package com.example.demo.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Category {
    private int categoryId;
    private String mainCategroyGroup;
    private String mainCategoryName;
    private String firstSubCateName;
    private String secondSubCateName;
}
