package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.address.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AddressProvider {

    private final AddressDao addressDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AddressProvider(AddressDao addressDao){
        this.addressDao = addressDao;
    }

    public List<Address> getAddressesByUserId(int userId) throws BaseException {
        try{
            List<Address> addresses = addressDao.getAddressesByUserId(userId);
            return addresses;
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public Address getAddressesByUserAddressId(int addressId) throws BaseException {
        try{
            Address addresse = addressDao.getAddressesByUserAddressId(addressId);
            return addresse;
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
