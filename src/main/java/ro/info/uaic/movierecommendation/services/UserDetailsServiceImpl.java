package ro.info.uaic.movierecommendation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.repos.UserRepo;

import static java.util.Collections.emptyList;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userEntityOptional = userRepo.findByUsername(username);
        if(username.contains("@")){
            userEntityOptional = userRepo.findByEmail(username);
        }
        if (userEntityOptional.isEmpty()) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database.");
        }
        UserEntity userEntity = userEntityOptional.get();
        log.info("User found in the database");
        if(username.contains("@")){
            return new User(userEntity.getEmail(), userEntity.getPassword(), emptyList());
        }
        return new User(userEntity.getUsername(), userEntity.getPassword(), emptyList());
    }

}
