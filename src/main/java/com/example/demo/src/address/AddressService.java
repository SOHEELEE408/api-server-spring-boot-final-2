package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.src.address.model.Address;
import com.example.demo.src.address.model.AddressDelReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AddressService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AddressDao addressDao;
    private final AddressProvider addressProvider;

    @Autowired
    public AddressService(AddressDao addressDao, AddressProvider addressProvider){
        this.addressDao = addressDao;
        this.addressProvider = addressProvider;
    }

    public void createAddress(Address address) throws BaseException {

        try{
            int result = addressDao.createAddress(address);

            if(result == 0){
                throw new BaseException(ADD_ADDRESS_FAIL);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAddress(Address address) throws BaseException {
        try{
            int result = addressDao.modifyAddress(address);

            if(result == 0){
                throw new BaseException(MODIFY_ADDRESS_FAIL);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deletedAddress(AddressDelReq addressDelReq) throws BaseException {
        try{
            int result = addressDao.deletedAddress(addressDelReq);

            if(result == 0){
                throw new BaseException(DELETE_ADDRESS_FAIL);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
