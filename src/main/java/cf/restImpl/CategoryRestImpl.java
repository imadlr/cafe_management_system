package cf.restImpl;

import cf.POJO.Category;
import cf.constents.CafeConstants;
import cf.rest.CategoryRest;
import cf.services.CategoryService;
import cf.utils.CafeUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
public class CategoryRestImpl implements CategoryRest {

    private CategoryService categoryService;

    @Override
    public ResponseEntity<String> saveCategory(Map<String, String> requestMap) {
        try {
            return categoryService.saveCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getCategories(String validate) {
        try {
            return categoryService.getCategories(validate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> mapRequest) {
        try {
            return categoryService.updateCategory(mapRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
