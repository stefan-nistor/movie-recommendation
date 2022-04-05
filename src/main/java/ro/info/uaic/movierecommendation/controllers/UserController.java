package ro.info.uaic.movierecommendation.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.info.uaic.movierecommendation.models.User;
import ro.info.uaic.movierecommendation.services.UserService;

@RequestMapping("api/v1/users")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void addPerson(@RequestBody User user){
        System.out.println("User Controller passed!");
        userService.addUser(user);
    }
}
