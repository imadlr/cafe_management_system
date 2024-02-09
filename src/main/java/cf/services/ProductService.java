package cf.services;

import cf.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

    ResponseEntity<String> saveProduct(Map<String, String> requestMap);

    ResponseEntity<List<ProductWrapper>> getProducts();

    ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    ResponseEntity<String> deleteProduct(Integer productId);

    ResponseEntity<String> updateProductStatus(Map<String, String> requestMap);

    ResponseEntity<List<ProductWrapper>> getProductsByCategory(Integer categoryId);

    ResponseEntity<ProductWrapper> getProduct(Integer productId);
}
