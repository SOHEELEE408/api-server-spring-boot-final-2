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
public class PostReviewReq {

    private int orderDetailId;
    private int userId;
    private String contents;
    private int score;
    private int reStep;
    private List<String> reviewImges;
}
