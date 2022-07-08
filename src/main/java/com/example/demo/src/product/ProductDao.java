package com.example.demo.src.product;

import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetProductRes> getProducts() {
        String getProductQuery = "select * from Products";
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("productId"),
                        rs.getInt("categoryId"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getString("comp"),
                        rs.getInt("weight"),
                        rs.getInt("discountRate"),
                        rs.getInt("forcastDeliveryRate"))
                );
    }

    public List<GetProductRes> getProductsByBrand(String brand) {
        String getProductsByBrandQuery = "select * from Products where brand = ?";
        String getProductParams = brand;
        return this.jdbcTemplate.query(getProductsByBrandQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("productId"),
                        rs.getInt("categoryId"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getString("comp"),
                        rs.getInt("weight"),
                        rs.getInt("discountRate"),
                        rs.getInt("forcastDeliveryRate")),
                getProductParams);
    }

    public List<GetProductRes> getProductsByCategory(int categoryId) {
        String getProductsByCategoryQuery = "select * from Products where categoryId = ?";
        int getProductByCategoryParams = categoryId;
        return this.jdbcTemplate.query(getProductsByCategoryQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("productId"),
                        rs.getInt("categoryId"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("deliveryType"),
                        rs.getString("comp"),
                        rs.getInt("weight"),
                        rs.getInt("discountRate"),
                        rs.getInt("forcastDeliveryRate")),
                getProductByCategoryParams);
    }

    public Product getProduct(int productId) {
        String getProductQuery = "select * from Products where productId = ?";
        int getProductParams = productId;
        return this.jdbcTemplate.queryForObject(getProductQuery,
                (rs, rowNum) -> new Product(
                        rs.getInt("productId"),
                        rs.getInt("categoryId"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getInt("deliveryFee"),
                        rs.getString("deliveryType"),
                        rs.getString("caurierCompany"),
                        rs.getString("brand"),
                        rs.getInt("stock"),
                        rs.getString("caution"),
                        rs.getString("comp"),
                        rs.getInt("weight"),
                        rs.getInt("discountRate"),
                        rs.getInt("forcastDeliveryRate")),
                getProductParams);
    }
}
