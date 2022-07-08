package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.cart.model.*;
import com.example.demo.src.order.OrderService;
import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/carts")
public class CartController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CartProvider cartProvider;

    @Autowired
    private final CartService cartService;

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtService jwtService;


    public CartController(CartProvider cartProvider, CartService cartService, OrderService orderService, UserService userService, JwtService jwtService){
        this.cartProvider = cartProvider;
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetCartRes>> getCarts(@PathVariable("userId") int userId){
        try{
            List<GetCartRes> getCartRes = cartProvider.getCarts(userId);
            return new BaseResponse<>(getCartRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/{userId}")
    public BaseResponse<String> createCart(@RequestBody Cart cart){
        if(!(cart.getUserId() > 0)){
            return new BaseResponse<>(POST_CARTS_NO_USER);
        }

        try{
            cartService.createCart(cart);

            String result = "장바구니에 성공적으로 담겼습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 주문생성 : 장바구니 -> 주문하기
    @ResponseBody
    @PostMapping("/{userId}/orders")
    public BaseResponse<PostCartOrderRes> createOrderFromCart(@PathVariable("userId") int userId, @RequestBody PostCartOrderReq postCartOrderReq){

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostCartOrderRes postCartOrderRes = orderService.createOrderFromCart(postCartOrderReq);

            return new BaseResponse<>(postCartOrderRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyCart(@RequestBody Cart cart){
        try{
            cartService.modifyCart(cart);
            String result = "변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/deleted")
    public BaseResponse<String> deleteCart(@RequestBody PatchCartReq patchCartReq){
        try{
            cartService.deleteCart(patchCartReq);
            String result = "장바구니 항목이 삭제되었습니다.";

            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

}
