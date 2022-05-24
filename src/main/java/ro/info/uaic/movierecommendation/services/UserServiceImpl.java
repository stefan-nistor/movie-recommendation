package ro.info.uaic.movierecommendation.services;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.dtoresponses.UserObj;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.EmailFormatException;
import ro.info.uaic.movierecommendation.exceptions.UserException;
import ro.info.uaic.movierecommendation.repos.UserRepo;
import ro.info.uaic.movierecommendation.util.Validator;

import java.util.Base64;
import java.util.Optional;

@Data
@Service
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
}
