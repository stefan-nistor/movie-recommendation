package ro.info.uaic.movierecommendation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.hashing.Hashing;
import ro.info.uaic.movierecommendation.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping
    ResponseEntity<?> createNewUser(@RequestBody UserDTO userDTO) {
        userDTO.setPassword(Hashing.doHashing(userDTO.getPassword()));
        userService.saveNewUser(userDTO);
        return ResponseEntity.ok().build();
    }

}
