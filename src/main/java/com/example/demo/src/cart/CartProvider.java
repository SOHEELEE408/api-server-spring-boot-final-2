package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.src.cart.model.Cart;
import com.example.demo.src.cart.model.GetCartRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = {BaseException.class} )
public class CartProvider {

    private final CartDao cartDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CartProvider(CartDao cartDao){
        this.cartDao = cartDao;
    }

    public int checkCart(Cart cart) throws BaseException {
        try{
            return cartDao.checkCart(cart);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCartRes> getCarts(int userId) throws BaseException {
        try{
            List<GetCartRes> getCartRes = cartDao.getCarts(userId);
            return getCartRes;
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetCartRes getCart(int cartId) throws BaseException {
        try{
            GetCartRes getCartRes = cartDao.getCart(cartId);
            return getCartRes;
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
