package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Review {
    private int reviewId;
    private int orderDetailId;
    private int userId;
    private int score;
    private String contents;
    private String status;
    private int reStep;
}
