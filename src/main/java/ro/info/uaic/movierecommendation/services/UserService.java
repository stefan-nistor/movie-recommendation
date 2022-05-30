package ro.info.uaic.movierecommendation.services;

import org.springframework.security.oauth2.core.user.OAuth2User;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.dtoresponses.UserObj;
import ro.info.uaic.movierecommendation.exceptions.EmailFormatException;
import ro.info.uaic.movierecommendation.exceptions.UserException;

public interface UserService {

    UserDTO saveNewUser(UserDTO user) throws EmailFormatException, UserException;
    UserDTO updateUser(UserDTO user);
    UserObj getUserForToken(String token);
    void saveOauth2User(OAuth2User user);
    UserDTO findUserByResetToken(String token);
    UserObj getUserById(Long id);
    UserObj updateUserForBody(Long id, UserDTO userDTO);
    void deleteUser(Long id);
//    UserDTO getPrincipalDetails();
}
