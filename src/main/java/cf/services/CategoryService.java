package cf.services;

import cf.POJO.Category;
import cf.rest.CategoryRest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ResponseEntity<String> saveCategory(Map<String, String> requestMap);

    ResponseEntity<List<Category>> getCategories(String validate);

    ResponseEntity<String> updateCategory(Map<String, String> mapRequest);
}
