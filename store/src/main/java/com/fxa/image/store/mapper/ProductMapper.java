package com.fxa.image.store.mapper;

import com.fxa.image.store.mode.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Options(flushCache = Options.FlushCachePolicy.TRUE)//设置了也无效 select for update 也无效
    @Select("SELECT * FROM product WHERE id = #{id} FOR UPDATE")
    @Results(value = {
            @Result(column = "product_name", property = "productName"),
            @Result(column = "image_urls", property = "imageUrls")
    })
    Product getProductById(Long id);

    @Select("SELECT * FROM product")
    @Results(value = {
            @Result(column = "product_name", property = "productName"),
            @Result(column = "image_urls", property = "imageUrls")
    })
    List<Product> getAllProducts();

    @Insert("INSERT INTO product (product_name, price, description, stock, image_urls) " +
            "VALUES (#{productName}, #{price}, #{description}, #{stock}, #{imageUrls})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertProduct(Product product);

    @Update("UPDATE product SET " +
            "product_name = #{productName}, " +
            "price = #{price}, " +
            "description = #{description}, " +
            "stock = #{stock}, " +
            "image_urls = #{imageUrls} " +
            "WHERE id = #{id}")
    void updateProduct(Product product);

    @Delete("DELETE FROM product WHERE id = #{id}")
    void deleteProduct(Integer id);
}
