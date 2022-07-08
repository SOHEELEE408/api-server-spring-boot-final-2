package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.address.AddressProvider;
import com.example.demo.src.address.model.Address;
import com.example.demo.src.order.model.GetOrderDetailRes;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class OrderProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;
    private final AddressProvider addressProvider;

    @Autowired
    public OrderProvider(OrderDao orderDao, AddressProvider addressProvider){
        this.orderDao = orderDao;
        this.addressProvider = addressProvider;
    }


    public List<GetOrderRes> getOrders(int userId) throws BaseException {
        try{
            List<GetOrderRes> getOrderRes = orderDao.getOrders(userId);
            return getOrderRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetOrderDetailRes getOrder(int orderId) throws BaseException {
        try{
            List<GetOrderRes> orderDetails = orderDao.getOrderDetails(orderId);
            Order order = orderDao.getOrder(orderId);
            Address address = addressProvider.getAddressesByUserAddressId(order.getAddressId());

            for(GetOrderRes orderDetail : orderDetails){
                String status = "";
                switch (orderDetail.getStatus()) {
                    case "waiting": status = "결제대기"; break;
                    case "completePayment": status = "결제완료"; break;
                    case "readyForDelivery": status = "배송준비중"; break;
                    case "shipping": status = "배송중"; break;
                    case "deliveryComplete": status = "배송완료"; break;
                    case "returnProcessing": status = "반품처리중"; break;
                    case "returnComplete": status = "반품완료"; break;
                    case "exchangeProcessing": status = "교환처리중"; break;
                    case "exchangeComplete": status = "교환완료"; break;
                    case "cancel": status = "주문취소"; break;
                    default: status = "주문완료"; break;
                }
                orderDetail.setStatus(status);
            }

            GetOrderDetailRes getOrderDetailRes = new GetOrderDetailRes(orderDetails, address,
                                                    order.getPayment(), order.getCompany(), order.getPaymentNumber(),
                                                    order.getTotalProductsPrice(), order.getDeliveryFee(), order.getDiscount(),
                                                    order.getTotalPaymentPrice());
            return getOrderDetailRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
