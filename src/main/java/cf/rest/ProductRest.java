package cf.rest;

import cf.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path="/product")
public interface ProductRest {

    @PostMapping(path="/add")
    ResponseEntity<String> saveProduct(@RequestBody Map<String,String> requestMap);

    @GetMapping(path="/getProduct/{productId}")
    ResponseEntity<ProductWrapper> getProduct(@PathVariable Integer productId);

    @GetMapping(path="/getAll")
    ResponseEntity<List<ProductWrapper>> getProducts();

    @PostMapping(path="/update")
    ResponseEntity<String> updateProduct(@RequestBody Map<String,String> requestMap);

    @PostMapping(path="/delete/{productId}")
    ResponseEntity<String> deleteProduct(@PathVariable Integer productId);

    @PostMapping(path="/updateStatus")
    ResponseEntity<String> updateProductStatus(@RequestBody Map<String,String> requestMap);

    @GetMapping(path="/getByCategory/{categoryId}")
    ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer categoryId);
}
