package com.example.demo.src.address;

import com.example.demo.src.address.model.Address;
import com.example.demo.src.address.model.AddressDelReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AddressDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createAddress(Address address) {
        String createAddressQuery = "insert into ShippingAddresses (userId, receivedName, postNumber, address, detailAddress, phone, request, defaultAddress ) values (?,?,?,?,?,?,?,?)";
        Object[] createAddressParams = new Object[]{address.getUserId(), address.getReceivedName(), address.getPostNumber(), address.getAddress(), address.getDetailAddress(), address.getPhone(), address.getRequest(), address.getDefaultAddress()};

        return this.jdbcTemplate.update(createAddressQuery, createAddressParams);
    }

    public List<Address> getAddressesByUserId(int userId) {
        String getAddressesByUserIdQuery = "select * from ShippingAddresses where userId = ? and status != 'deleted'";
        int getAddressesByUserIdParams = userId;
        return this.jdbcTemplate.query(getAddressesByUserIdQuery,
                (rs, rowNum) -> new Address(
                        rs.getInt("addressId"),
                        rs.getInt("userId"),
                        rs.getString("receivedName"),
                        rs.getString("postNumber"),
                        rs.getString("address"),
                        rs.getString("detailAddress"),
                        rs.getString("phone"),
                        rs.getString("request"),
                        rs.getInt("defaultAddress")),
                getAddressesByUserIdParams);
    }

    public Address getAddressesByUserAddressId(int addressId) {
        String getAddressesByUserAddressIdQuery = "select * from ShippingAddresses where addressId = ?";
        int getAddressesByUserAddressIdParams = addressId;
        return this.jdbcTemplate.queryForObject(getAddressesByUserAddressIdQuery,
                (rs, rowNum) -> new Address(
                        rs.getInt("addressId"),
                        rs.getInt("userId"),
                        rs.getString("receivedName"),
                        rs.getString("postNumber"),
                        rs.getString("address"),
                        rs.getString("detailAddress"),
                        rs.getString("phone"),
                        rs.getString("request"),
                        rs.getInt("defaultAddress")),
                getAddressesByUserAddressIdParams);
    }


    public int modifyAddress(Address address) {
        String modifyAddressQuery = "update ShippingAddresses set receivedName = ?, postNumber = ?, address = ?, detailAddress = ?, phone = ?, request = ?, defaultAddress = ? where addressId = ? and userId = ? ";
        Object[] modifyAddressParams = new Object[]{address.getReceivedName(), address.getPostNumber(), address.getAddress(), address.getDetailAddress(), address.getPhone(), address.getRequest(), address.getDefaultAddress(), address.getAddressId(), address.getUserId()};

        return this.jdbcTemplate.update(modifyAddressQuery, modifyAddressParams);
    }

    public int deletedAddress(AddressDelReq addressDelReq) {
        String deletedAddressQuery = "update ShippingAddresses set status = 'deleted' where addressId = ? and userId = ?";
        Object[] deletedAddressParams = new Object[]{addressDelReq.getAddressId(), addressDelReq.getUserId()};

        return this.jdbcTemplate.update(deletedAddressQuery, deletedAddressParams);
    }


}
