package com.omriratson.userhub;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@SpringBootApplication
public class UserhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserhubApplication.class, args);
    }


}

@Data
class LoginUser {

    String username;
    String password;

}

@RestController
@RequestMapping("/user-management")
class UserController {
    @PostMapping("/login")
    public String signin(@RequestBody LoginUser loginUser) {
        if (loginUser.username.equals("omri.ratson") && loginUser.password.equals("1234")) {
            return UUID.randomUUID().toString();
        } else {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401));
        }
    }
}
