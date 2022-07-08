package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetReview;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.Review;
import com.example.demo.src.review.model.ReviewImg;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ReviewProvider {

    private final ReviewDao reviewDao;
    private final JwtService jwtService;
    private final UserProvider userProvider;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService, UserProvider userProvider){
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }


    public GetReviewRes getReviewsByUserId(int userId) throws BaseException {
        try {
            GetReviewRes getReviewRes = new GetReviewRes();
            List<GetReview> getReviews = new ArrayList<>();

            // 유저 이름 조회
            getReviewRes.setUserName(userProvider.getUser(userId).getName());

            List<Review> reviews = reviewDao.getReviewsByUserId(userId);

            for(Review review : reviews){
                GetReview getReview = new GetReview();
                List<ReviewImg> imgs = reviewDao.getReviewImg(review.getReviewId());
                if(reviewDao.checkSellerReview(review.getOrderDetailId()) == 1) {
                    Review sellerReview = reviewDao.getSellerReview(review.getOrderDetailId());
                    getReview.setSellerReview(sellerReview);
                }
                getReview.setReview(review);
                getReview.setReviewImgs(imgs);

                getReviews.add(getReview);
            }

            getReviewRes.setReviews(getReviews);

            return getReviewRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
