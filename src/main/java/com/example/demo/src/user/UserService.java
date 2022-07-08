package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Transactional
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postUserReq.getEmail()) == 1 && !userProvider.checkStatus(postUserReq.getEmail()).equals("deleted")){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPw());
            postUserReq.setPw(pwd);
        } catch (Exception ignored) {
            logger.error("[ERROR Level] 비밀번호 암호화 실패");
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{

            int userId = userDao.createUser(postUserReq);
            return new PostUserRes(userId, postUserReq.getEmail());
        } catch (Exception exception) {
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserPassword(PatchPasswordReq patchPasswordReq) throws BaseException {

        String password;
        String newPwd;
        String encryptionPw = userDao.getPwdForModify(patchPasswordReq);
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(encryptionPw);
        } catch (Exception ignored) {
            logger.error("[ERROR Level] 비밀번호 복호화 실패");
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(patchPasswordReq.getPw().equals(password)){
            try{
                newPwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchPasswordReq.getNewPw());
                patchPasswordReq.setPw(newPwd);
                int result = userDao.modifyUserPassword(patchPasswordReq);
                if(result == 0){
                    throw new BaseException(MODIFY_FAIL_USERPASSWORD);
                }
            } catch (Exception ignored) {
                logger.error("[ERROR Level] 비밀번호 암호화 실패");
                throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
            }
        } else {
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(PASSWORD_ERROR);
        }
    }

    public void modifyUserEmail(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserEmail(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USEREMAIL);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserPhone(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserPhone(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERPHONE);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserPush(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserPush(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERPUSH);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyDefaultPayment(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyDefaultPayment(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_DEFAULTPAYMENT);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUser(PatchPasswordReq patchPasswordReq) throws BaseException{
        String password;
        String encryptionPw = userDao.getPwdForModify(patchPasswordReq);
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(encryptionPw);
        } catch (Exception ignored) {
            logger.error("[ERROR Level] 비밀번호 복호화 실패");
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(patchPasswordReq.getPw().equals(password)){
            try{
                int result = userDao.deleteUser(patchPasswordReq);
                if(result == 0){
                    throw new BaseException(USER_DELETE_FAIL);
                }
            } catch(Exception exception){
                logger.error("[ERROR Level] 데이터베이스 조회 실패");
                throw new BaseException(DATABASE_ERROR);
            }
        } else {
            throw new BaseException(PASSWORD_ERROR);
        }

    }

}
