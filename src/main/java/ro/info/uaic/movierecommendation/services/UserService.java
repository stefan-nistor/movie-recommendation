package ro.info.uaic.movierecommendation.services;

import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.exceptions.EmailFormatException;
import ro.info.uaic.movierecommendation.exceptions.UserException;

public interface UserService {

    UserDTO saveNewUser(UserDTO user) throws EmailFormatException, UserException;

}
