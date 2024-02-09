package cf.dao;

import cf.POJO.Product;
import cf.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

    @Query("SELECT new cf.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) FROM Product p")
    List<ProductWrapper> getProducts();

    @Modifying
    @Query("UPDATE Product p SET p.status=:status WHERE p.id=:productId")
    void updateStatus(@Param("status") String status, @Param("productId") Integer productId);

    @Query("SELECT new cf.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) FROM Product p WHERE p.category.id=:categoryId AND p.status='true'")
    List<ProductWrapper> getProductsByCategory(@Param("categoryId") Integer categoryId);

    @Query("SELECT new cf.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) FROM Product p WHERE p.id=:productId")
    ProductWrapper getProduct(@Param("productId") Integer productId);
}
