package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public GetUserRes getUser(int id){
        String getUserQuery = "select * from Users where id = ?";
        int getUserParams = id;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("profileImgUrl"),
                        rs.getString("wow"),
                        rs.getInt("isPushNotice")),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into Users (name, pw, phone, email, isPushNotice) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getPw(), postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getIsPushNotice()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()"; // 가장 최근에 insert된 row의 auto increment column 값
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from Users where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public String checkStatus(String email){
        String checkStatusQuery = "select status from Users where email = ?";
        String checkStatusParams = email;
        return this.jdbcTemplate.queryForObject(checkStatusQuery, String.class,
                checkStatusParams);
    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update Users set name = ? where id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getName(), patchUserReq.getId()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserPassword(PatchPasswordReq patchPasswordReq) {
        String modifyUserPasswordQuery = "update Users set pw = ? where id = ?";
        Object[] modifyUserPasswordParams = new Object[]{patchPasswordReq.getPw(), patchPasswordReq.getId()};

        return this.jdbcTemplate.update(modifyUserPasswordQuery, modifyUserPasswordParams);
    }

    public int modifyUserEmail(PatchUserReq patchUserReq) {
        String modifyUserEmailQuery = "update Users set email = ? where id = ?";
        Object[] modifyUserEmailParams = new Object[]{patchUserReq.getEmail(), patchUserReq.getId()};

        return this.jdbcTemplate.update(modifyUserEmailQuery, modifyUserEmailParams);
    }

    public int modifyUserPhone(PatchUserReq patchUserReq) {
        String modifyUserPhoneQuery = "update Users set phone = ? where id = ?";
        Object[] modifyUserPhoneParams = new Object[]{patchUserReq.getPhone(), patchUserReq.getId()};

        return this.jdbcTemplate.update(modifyUserPhoneQuery, modifyUserPhoneParams);
    }

    public int modifyUserPush(PatchUserReq patchUserReq) {
        String modifyUserPushQuery = "update Users set isPushNotice = ? where id = ?";
        Object[] modifyUserPushParams = new Object[]{patchUserReq.getIsPushNotice(), patchUserReq.getId()};

        return this.jdbcTemplate.update(modifyUserPushQuery, modifyUserPushParams);
    }

    public int modifyDefaultPayment(PatchUserReq patchUserReq) {
        String modifyDefaultPaymentQuery = "update Users set defaultPayment = ? where id =?";
        Object[] modifyDefaultPaymentParams = new Object[]{patchUserReq.getDefaultPayment(), patchUserReq.getId()};

        return this.jdbcTemplate.update(modifyDefaultPaymentQuery, modifyDefaultPaymentParams);
    }

    public int deleteUser(PatchPasswordReq patchPasswordReq) {
        String deleteUserQuery = "update Users set status = ? where id= ? and email = ?";
        Object[] deleteUserParams = new Object[]{"deleted", patchPasswordReq.getId(), patchPasswordReq.getEmail()};

        return this.jdbcTemplate.update(deleteUserQuery, deleteUserParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select * from Users where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("id"),
                        rs.getString("pw"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("profileImgUrl"),
                        rs.getString("wow"),
                        rs.getInt("isPushNotice"),
                        rs.getString("status")),
                getPwdParams
                );

    }

    public String getPwdForModify(PatchPasswordReq patchPasswordReq) {
        String getPwdForModifyQuery = "select pw from Users where id = ?";
        int getPwdForModifyParams = patchPasswordReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdForModifyQuery,
                (rs, rowNum) -> new String(rs.getString("pw")),
                getPwdForModifyParams);
    }


}
