package cf.rest;

import cf.POJO.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path="/category")
public interface CategoryRest {

    @PostMapping(path="/add")
    ResponseEntity<String> saveCategory(@RequestBody Map<String,String> requestMap);

    @GetMapping(path="/getAll")
    ResponseEntity<List<Category>> getCategories(@RequestParam(name = "validate",required = false) String validate);

    @PostMapping(path="/update")
    ResponseEntity<String> updateCategory(@RequestBody Map<String,String> mapRequest);

}
