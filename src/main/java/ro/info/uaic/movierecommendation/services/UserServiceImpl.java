package ro.info.uaic.movierecommendation.services;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.EmailFormatException;
import ro.info.uaic.movierecommendation.repos.UserRepo;
import ro.info.uaic.movierecommendation.util.Validator;

@Data
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDTO saveNewUser(UserDTO user) throws EmailFormatException {
        if(!Validator.validateEmailAddress(user.getEmail()))
            throw new EmailFormatException();
        userRepo.save(mapper.map(user, UserEntity.class));
        return user;
    }
}
