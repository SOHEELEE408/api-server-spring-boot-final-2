package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchReviewReq {
    private int reviewId;
    private int score;
    private String contents;
    private List<ReviewImg> reviewImgs;
}
