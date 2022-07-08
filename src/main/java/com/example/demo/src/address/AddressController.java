package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.address.model.Address;
import com.example.demo.src.address.model.AddressDelReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.POST_USERS_EMPTY_PHONE;

@RestController
@RequestMapping("/app/address")
public class AddressController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AddressProvider addressProvider;

    @Autowired
    private final AddressService addressService;

    @Autowired
    private final JwtService jwtService;

    public AddressController(AddressProvider addressProvider, AddressService addressService, JwtService jwtService){
        this.addressProvider = addressProvider;
        this.addressService = addressService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/{userId}")
    public BaseResponse<String> createAddress(@PathVariable("userId") int userId, @RequestBody Address address){

        if(address.getReceivedName() == null){
            return new BaseResponse<>(POST_ADDRESS_EMPTY_RECEIVEDNAME);
        }

        if(address.getPostNumber() == null){
            return new BaseResponse<>(POST_ADDRESS_EMPTY_POSTNUMBER);
        }

        if(address.getAddress() == null){
            return new BaseResponse<>(POST_ADDRESS_EMPTY_ADDRESS);
        }

        if(address.getPhone() == null){
            return new BaseResponse<>(POST_ADDRESS_EMPTY_PHONE);
        }

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            addressService.createAddress(address);

            String result = "배송지가 성공적으로 추가되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<Address>> getAddressesByUserId(@PathVariable("userId") int userId){
        try{
            List<Address> addresses = addressProvider.getAddressesByUserId(userId);
            return new BaseResponse<>(addresses);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/{addressId}")
    public BaseResponse<Address> getAddressesByUserAddressId(@PathVariable int addressId){
        try{
            Address addresse = addressProvider.getAddressesByUserAddressId(addressId);
            return new BaseResponse<>(addresse);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PutMapping("/{userId}")
    public BaseResponse<String> modifyAddress(@PathVariable("userId") int userId, @RequestBody Address address){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            addressService.modifyAddress(address);

            String result = "주소 변경이 완료되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/deleted")
    public BaseResponse<String> deletedAddress(@PathVariable("userId") int userId, @RequestBody AddressDelReq addressDelReq){

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            addressService.deletedAddress(addressDelReq);

            String result = "배송지가 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }
}
