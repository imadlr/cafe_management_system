package cf.serviceImpl;

import cf.dao.BillDao;
import cf.dao.CategoryDao;
import cf.dao.ProductDao;
import cf.services.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private CategoryDao categoryDao;
    private ProductDao productDao;
    private BillDao billDao;

    @Override
    public ResponseEntity<Map<String, Long>> getCount() {
        Map<String,Long> map = new HashMap<>();
        map.put("category",categoryDao.count());
        map.put("product",productDao.count());
        map.put("bill",billDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
