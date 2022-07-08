package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.cart.CartProvider;
import com.example.demo.src.cart.CartService;
import com.example.demo.src.cart.model.GetCartRes;
import com.example.demo.src.cart.model.PostCartOrderReq;
import com.example.demo.src.cart.model.PostCartOrderRes;
import com.example.demo.src.order.model.Order;
import com.example.demo.src.order.model.PatchOrderDetailReq;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.PatchUserReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderService{

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderProvider orderProvider;
    private final CartProvider cartProvider;
    private final CartService cartService;
    private final UserService userService;
    private final OrderDao orderDao;

    @Autowired
    public OrderService(OrderProvider orderProvider, CartProvider cartProvider, CartService cartService, UserService userService, OrderDao orderDao){
        this.orderProvider = orderProvider;
        this.cartProvider = cartProvider;
        this.cartService = cartService;
        this.userService = userService;
        this.orderDao = orderDao;
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public PostCartOrderRes createOrderFromCart(PostCartOrderReq postCartOrderReq) throws BaseException {

        try{
            // order 등록
            int orderId = createOrder(postCartOrderReq);

            // order 디테일 등록
            List<Integer> orderDetailIds = new ArrayList<>();

            for(int cartId : postCartOrderReq.getCartIds()){
                GetCartRes cart = cartProvider.getCart(cartId);

                int orderDetailId = createOrderDetail(cart, orderId);
                orderDetailIds.add(orderDetailId);

                // 주문한 cart 항목 status 변경
                cartService.modifyCartStatus(cartId);
            }

            // user 기본 결제 수단 업데이트
            if(postCartOrderReq.getIsDafaultPayment() == 1){
                PatchUserReq patchUserReq = new PatchUserReq();
                patchUserReq.setId(postCartOrderReq.getUserId());
                patchUserReq.setDefaultPayment(postCartOrderReq.getPayment());
                userService.modifyDefaultPayment(patchUserReq);
            }

            PostCartOrderRes postCartOrderRes = new PostCartOrderRes(orderId, orderDetailIds);
            return postCartOrderRes;

        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 등록 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public PostOrderRes createOrderFromProductDetail(PostOrderReq postOrderReq) throws BaseException{

        try{
            // order 등록
            PostCartOrderReq postCartOrderReq = new PostCartOrderReq(postOrderReq.getUserId(), postOrderReq.getAddressId(), postOrderReq.getTotalProductsPrice(), postOrderReq.getDiscount(),
                                                                    postOrderReq.getDeliveryFee(), postOrderReq.getCoupangCash(), postOrderReq.getTotalPaymentPrice(), postOrderReq.getPayment(),
                                                                    postOrderReq.getCompany(), postOrderReq.getPaymentNumber(), postOrderReq.getInstallments(), postOrderReq.getIsDafaultPayment(),
                                                                    postOrderReq.getCashReceiptNumber(), postOrderReq.getStatus());
            int orderId = createOrder(postCartOrderReq);

            // order 디테일 등록
            GetCartRes getCartRes = new GetCartRes(postOrderReq.getProductId(), postOrderReq.getQuantity(), postOrderReq.getDeliveryFee(), postOrderReq.getOptionId());
            int orderDetailId = createOrderDetail(getCartRes, orderId);

            // user 기본 결제 수단 업데이트
            if(postOrderReq.getIsDafaultPayment() == 1){
                PatchUserReq patchUserReq = new PatchUserReq();
                patchUserReq.setId(postCartOrderReq.getUserId());
                patchUserReq.setDefaultPayment(postOrderReq.getPayment());
                userService.modifyDefaultPayment(patchUserReq);
            }

            PostOrderRes postOrderRes = new PostOrderRes(orderId, orderDetailId);
            return  postOrderRes;

        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 등록 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int createOrder(PostCartOrderReq postCartOrderReq) throws BaseException {
        try{
            return orderDao.createOrder(postCartOrderReq);
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 등록 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }


    public int createOrderDetail(GetCartRes cart, int orderId) throws BaseException {

        try{
            return orderDao.createOrderDetail(cart, orderId);
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 등록 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void deleteOrderDetail(PatchOrderDetailReq patchOrderDetailReq) throws BaseException{
        try{
            // 주문 상세 변경
            int result = orderDao.deleteOrderDetail(patchOrderDetailReq);
            if(result == 0){

                throw new BaseException(PATCH_FAIL_CANCEL_ORDER);
            }

            Order order = orderDao.getOrder(patchOrderDetailReq.getOrderId());
            int modifyTotalPrice = order.getTotalPaymentPrice() - patchOrderDetailReq.getPrice()* patchOrderDetailReq.getQuantity() - patchOrderDetailReq.getDeliveryFee();
            if(modifyTotalPrice == 0){
                order.setTotalPaymentPrice(modifyTotalPrice);
                order.setStatus("cancelAll");
                result = orderDao.modifyOrderStatus(order);
            } else {
                order.setTotalPaymentPrice(modifyTotalPrice);
                order.setStatus("cancelPartial");
                result = orderDao.modifyOrderStatus(order);
            }

            if(result == 0){
                throw new BaseException(PATCH_FAIL_CANCEL_ORDER);
            }

        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 수정 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyOrderStatus(PatchOrderDetailReq patchOrderDetailReq) throws BaseException{
        try{
            int result = orderDao.modifyOrderStatusReturnOrExchange(patchOrderDetailReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_STATUS_ORDER);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 수정 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
