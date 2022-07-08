package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/reviews")
public class ReviewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;

    @Autowired
    private final ReviewService reviewService;

    @Autowired
    private final JwtService jwtService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    // 리뷰등록
    @ResponseBody
    @PostMapping("/{orderDetailId}/{userId}")
    public BaseResponse<String> createReview(@PathVariable int orderDetailId, @PathVariable int userId, @RequestBody PostReviewReq postReviewReq){
        if(postReviewReq.getScore() == 0){
            return new BaseResponse<>(POST_REVIEW_EMPTY_SCORE);
        }

        if(postReviewReq.getContents() == null){
            return new BaseResponse<>(POST_REVIEW_EMPTY_CONTENTS);
        }

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            postReviewReq.setUserId(userId);
            postReviewReq.setOrderDetailId(orderDetailId);

            reviewService.createReview(postReviewReq);

            String result = "후기가 등록되었습니다.";
            return new BaseResponse<>(result);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 리뷰조회
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetReviewRes> getReviewsByUserId(@PathVariable int userId){
        try{
            GetReviewRes getReviewRes = reviewProvider.getReviewsByUserId(userId);
            return new BaseResponse<>(getReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 리뷰수정
    @ResponseBody
    @PatchMapping("/{userId}/{reviewId}")
    public BaseResponse<String> modifyReview(@PathVariable int userId, @PathVariable int reviewId, @RequestBody PatchReviewReq patchReviewReq){
        if(patchReviewReq.getScore() == 0){
            return new BaseResponse<>(POST_REVIEW_EMPTY_SCORE);
        }

        if(patchReviewReq.getContents() == null){
            return new BaseResponse<>(POST_REVIEW_EMPTY_CONTENTS);
        }

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            patchReviewReq.setReviewId(reviewId);
            reviewService.modifyReview(patchReviewReq);

            String result = "후기가 수정되었습니다.";
            return new BaseResponse<>(result);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 리뷰삭제
    @ResponseBody
    @PatchMapping("/{reviewId}")
    public BaseResponse<String> deleteReview(@PathVariable int reviewId){
        try{
            reviewService.deleteReview(reviewId);
            String result = "리뷰 삭제 처리가 완료되었습니다.";
            return new BaseResponse<>(result);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
