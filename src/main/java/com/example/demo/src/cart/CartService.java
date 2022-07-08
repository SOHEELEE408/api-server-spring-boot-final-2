package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.src.cart.model.Cart;
import com.example.demo.src.cart.model.PatchCartReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class CartService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CartDao cartDao;
    private final CartProvider cartProvider;

    @Autowired
    public CartService(CartDao cartDao, CartProvider cartProvider){
        this.cartDao = cartDao;
        this.cartProvider = cartProvider;
    }

    public int createCart(Cart cart) throws BaseException {

        // 동일한 상품이 있는지 확인
        if(cartProvider.checkCart(cart) > 0){
            return modifyCart(cart);
        }

        try{
            return cartDao.createCart(cart);
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 등록 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int modifyCart(Cart cart) throws BaseException {
        try{
            int result = cartDao.modifyCart(cart);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_CART);
            }
            return result;
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 수정 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyCartStatus(int cartId) throws BaseException {
        try{
            int result = cartDao.modifyCartStatus(cartId);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_CART);
            }

        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 상태 수정 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteCart(PatchCartReq patchCartReq) throws BaseException {
        try{
            int result = cartDao.deleteCart(patchCartReq);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_CART);
            }
        } catch(Exception exception) {
            logger.error("[ERROR Level] 데이터베이스 수정 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
