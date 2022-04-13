package ro.info.uaic.movierecommendation.services;

import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;

@Service
public interface UserService {

    UserDTO saveNewUser(UserDTO user);

}
