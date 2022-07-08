package com.example.demo.src.order;

import com.example.demo.src.cart.model.GetCartRes;
import com.example.demo.src.cart.model.PostCartOrderReq;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.Order;
import com.example.demo.src.order.model.PatchOrderDetailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int createOrder(PostCartOrderReq postCartOrderReq) {
        String createOrderQuery = "insert into Orders (userId, addressId, totalProductsPrice, discount, deliveryFee, "
                + " coupangCash, totalPaymentPrice, payment, company, paymentNumber, "
                + " installments, isDafaultPayment, cashReceiptNumber, status) "
                +" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createOrderQueryParams = new Object[]{postCartOrderReq.getUserId(), postCartOrderReq.getAddressId(), postCartOrderReq.getTotalProductsPrice(), postCartOrderReq.getDiscount(), postCartOrderReq.getDeliveryFee(),
                                            postCartOrderReq.getCoupangCash(), postCartOrderReq.getTotalPaymentPrice(), postCartOrderReq.getPayment(), postCartOrderReq.getCompany(), postCartOrderReq.getPaymentNumber(),
                                            postCartOrderReq.getInstallments(), postCartOrderReq.getIsDafaultPayment(), postCartOrderReq.getCashReceiptNumber(), postCartOrderReq.getStatus()};

        this.jdbcTemplate.update(createOrderQuery, createOrderQueryParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);


    }

    public int createOrderDetail(GetCartRes cart, int orderId) {
        String createOrderDetailQuery = "insert into OrderDetails (orderId, productId, quantity, deliveryFee, optionId, status) values(?, ?, ?, ?, ?, 'completePayment') ";
        Object[] createOrderDetailQueryParams = new Object[]{orderId, cart.getProductId(), cart.getQuantity(), cart.getDeliveryFee(), cart.getOptionId()};

        this.jdbcTemplate.update(createOrderDetailQuery, createOrderDetailQueryParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public List<GetOrderRes> getOrders(int userId) {
        String getOrdersQuery = "SELECT od.orderId as orderId, od.orderDetailId as orderDetailId, od.status as status, od.quantity as quantity, o.payment as payment, op.optionDetail as productOption, p.name as productName, p.price as price from coupang.OrderDetails as od " +
                " left join coupang.Orders as o " +
                " on od.orderId = o.orderId " +
                " left join coupang.Options as op " +
                " on od.optionId = op.optionId " +
                " left join coupang.Products as p " +
                " on od.productId = p.productId " +
                " where o.userId = ? ";
        int getOrderQueryParam = userId;

        return this.jdbcTemplate.query(getOrdersQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getInt("orderId"),
                        rs.getInt("orderDetailId"),
                        rs.getString("status"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getString("payment"),
                        rs.getInt("quantity"),
                        rs.getString("productOption")),
                getOrderQueryParam);

    }

    public List<GetOrderRes> getOrderDetails(int orderId) {
        String getOrderDetailsQuery = "SELECT od.orderId as orderId, od.orderDetailId as orderDetailId, od.status as status, od.quantity as quantity, o.payment as payment, op.optionDetail as productOption, p.name as productName, p.price as price from coupang.OrderDetails as od " +
                " left join coupang.Orders as o " +
                " on od.orderId = o.orderId " +
                " left join coupang.Options as op " +
                " on od.optionId = op.optionId " +
                " left join coupang.Products as p " +
                " on od.productId = p.productId " +
                " where o.orderId = ? ";
        int getOrderDetailsParam = orderId;

        return this.jdbcTemplate.query(getOrderDetailsQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getInt("orderId"),
                        rs.getInt("orderDetailId"),
                        rs.getString("status"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getString("payment"),
                        rs.getInt("quantity"),
                        rs.getString("productOption")),
                getOrderDetailsParam);
    }

    public Order getOrder(int orderId) {
        String getOrderQuery = "select * from Orders where orderId = ? ";
        int getOrderQueryParam = orderId;

        return this.jdbcTemplate.queryForObject(getOrderQuery,
                (rs, rowNum) -> new Order(
                        rs.getInt("orderId"),
                        rs.getInt("userId"),
                        rs.getInt("addressId"),
                        rs.getInt("totalProductsPrice"),
                        rs.getInt("discount"),
                        rs.getInt("deliveryFee"),
                        rs.getInt("coupangCash"),
                        rs.getInt("totalPaymentPrice"),
                        rs.getString("payment"),
                        rs.getString("company"),
                        rs.getString("paymentNumber"),
                        rs.getInt("installments"),
                        rs.getInt("isDafaultPayment"),
                        rs.getString("cashReceiptNumber"),
                        rs.getString("status")),
                getOrderQueryParam);
    }


    public int deleteOrderDetail(PatchOrderDetailReq patchOrderDetailReq) {
        String deleteOrderDetailQuery = "update OrderDetails set status = 'cancel' where orderDetailId = ? ";
        int deleteOrderDetailQueryParam = patchOrderDetailReq.getOrderDetailId();

        return this.jdbcTemplate.update(deleteOrderDetailQuery, deleteOrderDetailQueryParam);
    }

    public int modifyOrderStatus(Order order) {
        String modifyOrderStatusQuery = "update Orders set totalPaymentPrice = ?, status = ? where orderId = ?";
        Object[] modifyOrderStatusQueryParams = new Object[]{order.getTotalPaymentPrice(), order.getStatus(), order.getOrderId()};

        return this.jdbcTemplate.update(modifyOrderStatusQuery, modifyOrderStatusQueryParams);
    }

    public int modifyOrderStatusReturnOrExchange(PatchOrderDetailReq patchOrderDetailReq) {
        String modifyOrderStatusReturnOrExchangeQuery = "update OrderDetails set status = ? where orderDetailId = ? ";
        Object[] modifyOrderStatusReturnOrExchangeQueryParams = new Object[]{patchOrderDetailReq.getNewStatus(), patchOrderDetailReq.getOrderDetailId()};

        return this.jdbcTemplate.update(modifyOrderStatusReturnOrExchangeQuery, modifyOrderStatusReturnOrExchangeQueryParams);
    }
}
