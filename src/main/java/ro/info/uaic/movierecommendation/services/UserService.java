package ro.info.uaic.movierecommendation.services;

import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.exceptions.EmailFormatException;

public interface UserService {

    UserDTO saveNewUser(UserDTO user) throws EmailFormatException;

}
