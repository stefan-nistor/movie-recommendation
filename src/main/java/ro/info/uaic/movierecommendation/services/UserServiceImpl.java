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
import ro.info.uaic.movierecommendation.dtoresponses.UserObj;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.EmailFormatException;
import ro.info.uaic.movierecommendation.exceptions.UserException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
import ro.info.uaic.movierecommendation.repos.UserRepo;
import ro.info.uaic.movierecommendation.util.Validator;

import java.util.Base64;
import java.util.Optional;

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
    public UserObj getUserForToken(String token) {
        token = token.split(" ")[1];
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        int index = payload.indexOf("sub");
        String tempStr = payload.substring(index + "sub".length() + 3);
        index = tempStr.indexOf("\"");
        tempStr = tempStr.substring(0, index);

        Optional<UserEntity> user = userRepo.findByUsername(tempStr);

        return mapper.map(user, UserObj.class);
    }
    
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

    @Override
    public UserObj getUserById(Long id) {
        Optional<UserEntity> user = userRepo.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found for this id.");
        }

        return mapper.map(user, UserObj.class);
    }

    @Override
    public UserObj updateUserForBody(Long id, UserDTO userDTO) {
        if (!userRepo.existsById(id)) {
            throw new UserNotFoundException("User not found for this id.");
        }
        UserEntity user = userRepo.getById(id);
        userDTO.setId(id);
        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getFirstname() != null) {
            user.setFirstname(userDTO.getFirstname());
        }
        if (userDTO.getLastname() != null) {
            user.setLastname(userDTO.getLastname());
        }

        userRepo.save(user);

        return mapper.map(userDTO, UserObj.class);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<UserEntity> user = userRepo.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found for this id.");
        }
        userRepo.delete(user.get());
    }

}
