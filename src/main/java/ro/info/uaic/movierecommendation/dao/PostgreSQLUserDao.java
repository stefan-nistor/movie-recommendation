package ro.info.uaic.movierecommendation.dao;

import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.models.Email;
import ro.info.uaic.movierecommendation.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("PostgreSQL")
public class PostgreSQLUserDao implements UserDao{

    private List<User> DB = new ArrayList<>();

    @Override
    public int insertUser(UUID id, User user) {
        DB.add(new User(user.getUserName(), user.getPassword(), user.geteMail(), id));
        return 1;
    }

}
