package cf.dao;

import cf.POJO.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category,Integer> {

    @Query("SELECT c FROM Category c WHERE c IN (SELECT p.category FROM Product p WHERE p.status = 'true')")
    List<Category> getCategories();

}
