package com.example.demo.src.cart;

import com.example.demo.src.cart.model.Cart;
import com.example.demo.src.cart.model.GetCartRes;
import com.example.demo.src.cart.model.PatchCartReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CartDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int checkCart(Cart cart) {
        String checkCartQuery = "select exists(select cartId from Carts where productId = ? and userId = ?)";
        Object[] checkCartParams = new Object[]{cart.getProductId(), cart.getUserId()};
        return this.jdbcTemplate.queryForObject(checkCartQuery,
                int.class, checkCartParams);
    }

    public int modifyCart(Cart cart) {
        String modifyCartQuery = "update Carts set quantity = ?, optionId = ? where userId = ? and productId = ?";
        Object[] modifyCartParams = new Object[]{cart.getQuantity(), cart.getOptionId(), cart.getUserId(), cart.getProductId()};

        return this.jdbcTemplate.update(modifyCartQuery, modifyCartParams);
    }

    public int modifyCartStatus(int cartId) {
        String modifyCartStatusQuery = "update Carts set status = 'order' where cartId = ?";
        int modifyCartStatusParam = cartId;

        return this.jdbcTemplate.update(modifyCartStatusQuery, modifyCartStatusParam);
    }

    public int createCart(Cart cart) {
        String createCartQuery = "insert into Carts (userId, productId, quantity, optionId) values(?,?,?,?)";
        Object[] createCartQueryParams = new Object[]{cart.getUserId(), cart.getProductId(), cart.getQuantity(), cart.getOptionId()};
        this.jdbcTemplate.update(createCartQuery, createCartQueryParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int deleteCart(PatchCartReq patchCartReq) {
        String deleteCartQuery = "update Carts set status = 'cancel' where userId = ? and cartId = ?";
        Object[] deleteCartParams = new Object[]{patchCartReq.getUserId(), patchCartReq.getCartId()};

        return this.jdbcTemplate.update(deleteCartQuery, deleteCartParams);
    }

    public List<GetCartRes> getCarts(int userId) {
        String getCartsQuery = " select c.cartid, c.productid, p.name, c.quantity, c.optionId, TRUNCATE((c.quantity * (p.price * (1-p.discountRate*0.01))), 0) as totalPrice, p.deliveryType, p.deliveryFee, p.freeShippingStandard " +
                " from Carts as c " +
                " left join Products as p" +
                " on c.productId = p.productId " +
                " where c.userId = ? and c.status = 'select' ";
        int getCartsParams = userId;
        return this.jdbcTemplate.query(getCartsQuery,
                (rs, rowNum) -> new GetCartRes(
                        rs.getInt("cartId"),
                        rs.getInt("productId"),
                        rs.getInt("optionId"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getInt("totalPrice"),
                        rs.getString("deliveryType"),
                        rs.getInt("deliveryFee"),
                        rs.getInt("freeShippingStandard")),
                getCartsParams);
    }

    public GetCartRes getCart(int cartId) {
        String getCartQuery = " select c.cartid, c.productid, p.name, c.quantity, c.optionId, TRUNCATE((c.quantity * (p.price * (1-p.discountRate*0.01))), 0) as totalPrice, p.deliveryType, p.deliveryFee, p.freeShippingStandard " +
                " from Carts as c " +
                " left join Products as p" +
                " on c.productId = p.productId " +
                " where c.cartId = ? ";
        int getCartParam = cartId;
        return this.jdbcTemplate.queryForObject(getCartQuery,
                (rs, rowNum) -> new GetCartRes(
                        rs.getInt("cartId"),
                        rs.getInt("productId"),
                        rs.getInt("optionId"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getInt("totalPrice"),
                        rs.getString("deliveryType"),
                        rs.getInt("deliveryFee"),
                        rs.getInt("freeShippingStandard")),
                getCartParam);
    }

}
