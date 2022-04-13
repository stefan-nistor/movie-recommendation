package ro.info.uaic.movierecommendation.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.uaic.movierecommendation.entites.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

}
