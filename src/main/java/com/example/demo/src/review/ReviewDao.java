package com.example.demo.src.review;

import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.Review;
import com.example.demo.src.review.model.ReviewImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createReview(PostReviewReq postReviewReq) {
        String createReviewQuery = " insert into Reviews (orderDetailId, score, contents, userId, reStep) values (?,?,?,?,?) ";
        Object[] createReviewQueryParams = new Object[]{postReviewReq.getOrderDetailId(), postReviewReq.getScore(), postReviewReq.getContents(), postReviewReq.getUserId(), postReviewReq.getReStep()};
        this.jdbcTemplate.update(createReviewQuery, createReviewQueryParams);

        String lastInserIdQuery = "select last_insert_id()"; // 가장 최근에 insert된 row의 auto increment column 값
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public void createRImage(String img, int reviewId) {
        String createRImageQuery = "insert into RImages (reviewId, imgUrl) values (?, ?) ";
        Object[] createRImageQueryParams = new Object[]{reviewId, img};

        this.jdbcTemplate.update(createRImageQuery, createRImageQueryParams);
    }

    public List<Review> getReviewsByUserId(int userId) {
        String getReviewsByUserIdQuery = "select * from Reviews where userId = ? and status = 'active'";
        int getReviewsByUserIdQueryParam  = userId;

        return this.jdbcTemplate.query(getReviewsByUserIdQuery,
                (rs, rowNum) -> new Review(
                        rs.getInt("reviewId"),
                        rs.getInt("orderDetailId"),
                        rs.getInt("userId"),
                        rs.getInt("score"),
                        rs.getString("contents"),
                        rs.getString("status"),
                        rs.getInt("reStep")),
                getReviewsByUserIdQueryParam);
    }

    public List<ReviewImg> getReviewImg(int reviewId) {
        String getReviewImgQuery = "select * from RImages where reviewId = ? and status = 'active'";
        int getReviewImgQueryParam = reviewId;

        return this.jdbcTemplate.query(getReviewImgQuery,
                (rs, rowNum) -> new ReviewImg(
                        rs.getInt("reviewImgId"),
                        rs.getString("imgUrl")),
                getReviewImgQueryParam);
    }

    public Review getSellerReview(int orderDetailId) {
        String getSellerReviewQuery = "select * from Reviews where orderDetailId = ? and reStep = 1 and status = 'active'";
        int getSellerReviewQueryParam  = orderDetailId;

        return this.jdbcTemplate.queryForObject(getSellerReviewQuery,
                (rs, rowNum) -> new Review(
                        rs.getInt("reviewId"),
                        rs.getInt("orderDetailId"),
                        rs.getInt("userId"),
                        rs.getInt("score"),
                        rs.getString("contents"),
                        rs.getString("status"),
                        rs.getInt("reStep")),
                getSellerReviewQueryParam);
    }

    public int checkSellerReview(int orderDetailId) {
        String checkSellerReviewQuery = "select exists(select * from Reviews where orderDetailId = ? and reStep = 1 and status = 'active')";
        int checkSellerReviewQueryParam = orderDetailId;

        return this.jdbcTemplate.queryForObject(checkSellerReviewQuery, int.class, checkSellerReviewQueryParam);
    }

    public String checkReviewStatus(int reviewId) {
        String checkReviewStatusQuery = "select status from Reviews where reviewId = ? ";
        int checkReviewStatusQueryParam = reviewId;

        return this.jdbcTemplate.queryForObject(checkReviewStatusQuery, String.class, checkReviewStatusQueryParam);
    }

    public int modifyReview(PatchReviewReq patchReviewReq) {
        String modifyReviewQuery = "update Reviews set score = ?, contents = ? where reviewId = ?";
        Object[] modifyReviewQueryParams = new Object[]{patchReviewReq.getScore(), patchReviewReq.getContents(), patchReviewReq.getReviewId()};

        return this.jdbcTemplate.update(modifyReviewQuery, modifyReviewQueryParams);
    }

    public int modifyReviewImg(ReviewImg img) {
        String modifyReviewImgQuery = "update RImages set imgUrl = ? where reviewImgId = ?";
        Object[] modifyReviewImgQueryParams = new Object[]{img.getImgUrl(), img.getReviewImgId()};

        return this.jdbcTemplate.update(modifyReviewImgQuery, modifyReviewImgQueryParams);
    }


    public int deleteReviewImgs(int reviewId) {
        String deleteReviewImgsQuery = "update RImages set status = 'deleted' where reviewId = ?";
        int deleteReviewImgsQueryParam = reviewId;

        return this.jdbcTemplate.update(deleteReviewImgsQuery, deleteReviewImgsQueryParam);
    }

    public int deleteReview(int reviewId) {
        String deleteReviewQuery = "update Reviews set status = 'deleted' where reviewId = ?";
        int deleteReviewQueryParam = reviewId;

        return this.jdbcTemplate.update(deleteReviewQuery, deleteReviewQueryParam);
    }
}
