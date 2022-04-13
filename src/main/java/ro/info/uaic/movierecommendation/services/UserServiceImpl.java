package ro.info.uaic.movierecommendation.services;

import org.springframework.beans.factory.annotation.Autowired;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.repos.UserRepo;

public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDTO saveNewUser(UserDTO user) {
        var entity = UserEntity.builder() // TODO - add fields for entity
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())   // pls save encrypted password
                .build();
        userRepo.save(entity);
        return user;
    }
}
