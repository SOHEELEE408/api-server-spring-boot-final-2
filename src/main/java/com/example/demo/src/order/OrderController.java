package com.example.demo.src.order;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/app/orders")
public class OrderController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final OrderProvider orderProvider;

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final JwtService jwtService;

    public OrderController(OrderProvider orderProvider, OrderService orderService, JwtService jwtService){
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }


    // 상품 상세에서 바로 구매
    @ResponseBody
    @PostMapping("/{productId}")
    public BaseResponse<PostOrderRes> createOrderFromProductDetail(@PathVariable int productId, @RequestBody PostOrderReq postOrderReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postOrderReq.getUserId() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostOrderRes postOrderRes = orderService.createOrderFromProductDetail(postOrderReq);
            return new BaseResponse<>(postOrderRes);

        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 전체 주문내역 조회
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetOrderRes>> getOrders(@PathVariable int userId){
        try{
            List<GetOrderRes> getOrderRes = orderProvider.getOrders(userId);
            return new BaseResponse<>(getOrderRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/{orderId}")
    public BaseResponse<GetOrderDetailRes> getOrder(@PathVariable int orderId){
        try{
            GetOrderDetailRes getOrderDetailRes = orderProvider.getOrder(orderId);

            return new BaseResponse<>(getOrderDetailRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ResponseBody
    @PatchMapping("/{userId}/{orderId}/{orderDetailId}")
    public BaseResponse<String> deleteOrderDetail(@PathVariable int userId, @PathVariable int orderDetailId, @RequestBody PatchOrderDetailReq patchOrderDetailReq){
        try{
            if(!(patchOrderDetailReq.getStatus().equals("waiting") || patchOrderDetailReq.getStatus().equals("completePayment") || patchOrderDetailReq.getStatus().equals("readyForDelivery"))){
                return new BaseResponse<>(PATCH_ORDER_INVALID_STATUS);
            }

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            orderService.deleteOrderDetail(patchOrderDetailReq);
            String result = "주문 취소 신청이 완료되었습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/{orderDetailId}")
    public BaseResponse<String> modifyOrderStatus (@PathVariable int userId, @PathVariable int orderDetailId, @RequestBody PatchOrderDetailReq patchOrderDetailReq){
        try{
            if(!patchOrderDetailReq.getStatus().equals("deliveryComplete")){
                return new BaseResponse<>(PATCH_ORDER_INVALID_STATUS);
            }

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            orderService.modifyOrderStatus(patchOrderDetailReq);
            String result = "주문 교환/반품 신청이 완료되었습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }


}
