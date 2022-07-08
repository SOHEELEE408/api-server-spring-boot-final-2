package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;

    public ProductController(ProductProvider productProvider){
        this.productProvider = productProvider;
    }

    // 상품 브랜드 별 조회, 상품 전체 조회
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProductRes>> getProducts(@RequestParam(required = false) String brand){
        try{
            if(brand == null){
                List<GetProductRes> getProductRes = productProvider.getProducts();
                return new BaseResponse<>(getProductRes);
            }

            List<GetProductRes> getProductRes = productProvider.getProductsByBrand(brand);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 카테고리 별 상품 조회
    @ResponseBody
    @GetMapping("/category/{categoryId}")
    public BaseResponse<List<GetProductRes>> getProductsByCategory(@PathVariable int categoryId){
        try{
            List<GetProductRes> getProductRes = productProvider.getProductsByCategory(categoryId);
            return new BaseResponse<>(getProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 상품 1개 조회
    @ResponseBody
    @GetMapping("/{productId}")
    public BaseResponse<Product> getProduct(@PathVariable int productId){
        try{
            Product product = productProvider.getProduct(productId);
            return new BaseResponse<>(product);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
