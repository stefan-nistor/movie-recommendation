package ro.info.uaic.movierecommendation.dao;

import ro.info.uaic.movierecommendation.models.Email;
import ro.info.uaic.movierecommendation.models.User;

import java.util.UUID;

public interface UserDao {
    int insertUser(UUID id, User user);

    default int insertUser(User user){
        UUID newId = UUID.randomUUID();
        return insertUser(newId, user);
    }

}
