package ro.info.uaic.movierecommendation.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.EmailFormatException;
import ro.info.uaic.movierecommendation.exceptions.UserException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
import ro.info.uaic.movierecommendation.repos.UserRepo;
import ro.info.uaic.movierecommendation.util.Validator;

@Data
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDTO saveNewUser(UserDTO user) {
        if (!Validator.validateEmailAddress(user.getEmail()))
            throw new EmailFormatException();
        var userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb.isPresent())
            throw new UserException("There is already a user with this username in database.");
        userRepo.save(mapper.map(user, UserEntity.class));
        return user;
    }

    @Override
    public UserDTO updateUser(UserDTO user) {
        var userFromDb = userRepo.findByUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No such user for given username"));

        userRepo.save(userFromDb);
        return user;
    }

    @Override
    public void saveOauth2User(OAuth2User user) {
        var entity = userRepo.findByEmail(user.getAttribute("email"));
        if (entity.isPresent()) {
            log.info("OAuth2 user {} already in database", user);
            return;
        }

        userRepo.save(UserEntity.builder()
                .username(user.getAttribute("email"))
                .email(user.getAttribute("email"))
                .firstname(user.getAttribute("given_name"))
                .lastname(user.getAttribute("family_name"))
                .build()
        );
        log.info("OAuth2 user {} saved in database.", user);
    }

    @Override
    public UserDTO findUserByResetToken(String token) {
        var result = userRepo.findByPasswordToken(token).orElseThrow(() ->  new UserNotFoundException("user not found"));
        return mapper.map(result, UserDTO.class);
    }

}
