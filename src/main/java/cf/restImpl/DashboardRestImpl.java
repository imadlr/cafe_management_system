package cf.restImpl;

import cf.rest.DashboardRest;
import cf.services.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class DashboardRestImpl implements DashboardRest {

    private DashboardService dashboardService;

    @Override
    public ResponseEntity<Map<String, Long>> getDetails() {
        return dashboardService.getCount();
    }
}
