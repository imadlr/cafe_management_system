package cf.rest;

import cf.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path="/user")
public interface UserRest {

    @PostMapping(path="/signup")
    ResponseEntity<String> signUp(@RequestBody Map<String,String> requestMap);

    @PostMapping(path = "/login")
     ResponseEntity<String> login(@RequestBody Map<String,String> reqyestMap);

    @GetMapping(path="/getUsers")
     ResponseEntity<List<UserWrapper>> getAllUser();

    @PostMapping(path="/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String,String> requestMap);

    @GetMapping(path="checkToken")
    ResponseEntity<String> checkToken();

    @PostMapping(path="/changePassword")
    ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestMap);

    @PostMapping(path="/forgotPassword")
    ResponseEntity<String> forgotPassword(@RequestBody Map<String,String> requestMap);

}
