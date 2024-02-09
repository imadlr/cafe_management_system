package cf.serviceImpl;

import cf.JWT.JwtFilter;
import cf.POJO.Category;
import cf.POJO.Product;
import cf.constents.CafeConstants;
import cf.dao.CategoryDao;
import cf.dao.ProductDao;
import cf.services.ProductService;
import cf.utils.CafeUtils;
import cf.wrapper.ProductWrapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductDao productDao;
    private JwtFilter jwtFilter;
    private CategoryDao categoryDao;

    @Override
    public ResponseEntity<String> saveProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                productDao.save(getProductFromMap(requestMap, true));
                return CafeUtils.getResponseEntity("Product Saved Successfully", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity("You are not authorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProducts() {
        try {
            return new ResponseEntity<>(productDao.getProducts(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (productDao.findById(Integer.parseInt(requestMap.get("id"))).isPresent()) {
                    productDao.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Product Updated Successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Product doesn't exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity("You are not authorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer productId) {
        try {
            if (jwtFilter.isAdmin()) {
                if (productDao.findById(productId).isPresent()) {
                    productDao.deleteById(productId);
                    return CafeUtils.getResponseEntity("Product deleted successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Product doesn't exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity("You are not authorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> product = productDao.findById(Integer.parseInt(requestMap.get("id")));
                if (product.isPresent()) {
                    productDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return CafeUtils.getResponseEntity("Product updated successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Product doesn't exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity("You are not authorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductsByCategory(Integer categoryId) {
        try {
            return new ResponseEntity<List<ProductWrapper>>(productDao.getProductsByCategory(categoryId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProduct(Integer productId) {
        try {
            return new ResponseEntity<>(productDao.getProduct(productId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Product product = new Product();
        Category category = categoryDao.findById(Integer.parseInt(requestMap.get("categoryId"))).get();
        if (!isAdd) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        }
        product.setName(requestMap.get("name"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        product.setDescription(requestMap.get("description"));
        product.setStatus(requestMap.get("status"));
        product.setCategory(category);
        return product;
    }
}
