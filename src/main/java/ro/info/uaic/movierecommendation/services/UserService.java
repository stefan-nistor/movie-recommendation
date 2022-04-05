package ro.info.uaic.movierecommendation.services;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dao.UserDao;
import ro.info.uaic.movierecommendation.models.User;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(@Qualifier("PostgreSQL") UserDao userDao) {
        this.userDao = userDao;
    }

    public int addUser(User user){
        System.out.println("User Service passed!");
        return userDao.insertUser(user);
    }
}
