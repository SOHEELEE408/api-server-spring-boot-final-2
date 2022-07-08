package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/categories")
public class CategoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CategoryProvider categoryProvider;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final JwtService jwtService;



    public CategoryController(CategoryProvider categoryProvider, CategoryService categoryService, JwtService jwtService) {
        this.categoryProvider = categoryProvider;
        this.categoryService = categoryService;
        this.jwtService = jwtService;
    }


    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCategoryRes>> getCategories() {
        try{

            List<GetCategoryRes> getCategoryRes = categoryProvider.getCategories();
            return new BaseResponse<>(getCategoryRes);

        } catch(BaseException exception){
            logger.error("[ERROR Level] 전체 카테고리 조회 실패");
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
