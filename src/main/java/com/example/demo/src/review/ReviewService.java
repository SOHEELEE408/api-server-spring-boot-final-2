package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.ReviewImg;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.SQLException;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewService {
    private final ReviewProvider reviewProvider;
    private final ReviewDao reviewDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewService(ReviewProvider reviewProvider, ReviewDao reviewDao, JwtService jwtService){
        this.reviewProvider= reviewProvider;
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }


    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void createReview(PostReviewReq postReviewReq) throws BaseException {
        try{
            // 리뷰 등록
            int reviewId = reviewDao.createReview(postReviewReq);

            // 리뷰이미지 등록
            for(String img : postReviewReq.getReviewImges()){
                reviewDao.createRImage(img, reviewId);
            }
        } catch (Exception exception) {
            logger.error("[ERROR Level] 데이터베이스 등록 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void modifyReview(PatchReviewReq patchReviewReq) throws BaseException {
        try{
            if(reviewDao.checkReviewStatus(patchReviewReq.getReviewId()).equals("deleted")){
                throw new BaseException(DELETED_REVIEW);
            }

            int result = reviewDao.modifyReview(patchReviewReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_REVIEW);
            }

            for(ReviewImg img: patchReviewReq.getReviewImgs()){
                result = reviewDao.modifyReviewImg(img);
                if(result == 0){
                    throw new BaseException(MODIFY_FAIL_REVIEW);
                }
            }

        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 수정 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void deleteReview(int reviewId) throws BaseException{
        try{
            if(reviewDao.checkReviewStatus(reviewId).equals("deleted")){
                throw new BaseException(DELETED_REVIEW);
            }

            int result = reviewDao.deleteReviewImgs(reviewId);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_REVIEW);
            }

            result = reviewDao.deleteReview(reviewId);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_REVIEW);
            }
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 수정 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
