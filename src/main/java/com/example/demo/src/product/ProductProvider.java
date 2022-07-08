package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.Product;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;


@Service
public class ProductProvider {
    
    private final ProductDao productDao;
    
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    public ProductProvider(ProductDao productDao){
        this.productDao = productDao;
    }

    public List<GetProductRes> getProducts() throws BaseException {
        try{
            return productDao.getProducts();
        } catch (Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductRes> getProductsByBrand(String brand) throws BaseException{
        try{
            List<GetProductRes> getProductRes = productDao.getProductsByBrand(brand);
            return getProductRes;
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductRes> getProductsByCategory(int categoryId) throws BaseException{
        try{
            List<GetProductRes> getProductRes = productDao.getProductsByCategory(categoryId);
            return getProductRes;
        } catch(Exception exception){
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public Product getProduct(int productId) throws BaseException{
        try{
            Product product = productDao.getProduct(productId);
            if(product == null){
                logger.warn("[WARN Level] 데이터베이스에 없는 상품 조회");
                throw new BaseException(RESPONSE_ERROR);
            }

            return product;
        } catch(Exception exception){
            System.out.println(exception);
            logger.error("[ERROR Level] 데이터베이스 조회 실패");
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
