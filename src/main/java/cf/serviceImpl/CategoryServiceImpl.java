package cf.serviceImpl;

import cf.JWT.JwtFilter;
import cf.POJO.Category;
import cf.constents.CafeConstants;
import cf.dao.CategoryDao;
import cf.rest.CategoryRest;
import cf.services.CategoryService;
import cf.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao;
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> saveCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                categoryDao.save(getCategoryFromMap(requestMap, true));
                return CafeUtils.getResponseEntity("Category Saved Successfully", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity("You are not authorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getCategories(String validate) {
        try {
            if(!Strings.isNullOrEmpty(validate) && validate.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Category>>(categoryDao.getCategories(), HttpStatus.OK);
            }else {
                return new ResponseEntity<List<Category>>(categoryDao.findAll(), HttpStatus.OK);
            }
             } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> mapRequest) {
        try {
            if (jwtFilter.isAdmin()) {
                if (!categoryDao.findById(Integer.parseInt(mapRequest.get("id"))).isPresent()) {
                    return CafeUtils.getResponseEntity("Category doesn't exist", HttpStatus.BAD_REQUEST);
                } else {
                    categoryDao.save(getCategoryFromMap(mapRequest, false));
                    return CafeUtils.getResponseEntity("Category Updated Successfully", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity("You are not authorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
        Category category = new Category();
        if (!isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }
}
