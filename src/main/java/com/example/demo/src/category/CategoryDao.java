package com.example.demo.src.category;

import com.example.demo.src.category.model.GetCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CategoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetCategoryRes> getCategories() {
        String getCategoriesQuery = "select second.*, sub.mainCategoryName, sub.firstSubCateName from SecondSubCategories as second " +
                "LEFT join (" +
                "    select firstC.*, mainC.mainCategoryName" +
                "    from FirstSubCategories as firstC" +
                "    left join MainCategories as mainC" +
                "    on firstC.mainCategoryId = mainC.mainCategoryId) as sub " +
                "on sub.firstSubCateId = second.firstSubCateId;";
        return this.jdbcTemplate.query(getCategoriesQuery,
                (rs,rownum) -> new GetCategoryRes(
                        rs.getInt("secondSubCatId"),
                        rs.getString("secondSubCateName"),
                        rs.getString("mainCategoryName"),
                        rs.getString("firstSubCatename"))
                );
    }

}
