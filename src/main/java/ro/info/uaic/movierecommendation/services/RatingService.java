package ro.info.uaic.movierecommendation.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.UserMovieLabelDto;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.entites.UserMovieLabel;
import ro.info.uaic.movierecommendation.exceptions.RatingNotFoundException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
import ro.info.uaic.movierecommendation.repos.RatingRepository;
import ro.info.uaic.movierecommendation.repos.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper mapper;

    public List<UserMovieLabelDto> getAllRatings() {
        return ratingRepo.findAll().stream()
                .map(rating -> mapper.map(rating, UserMovieLabelDto.class))
                .collect(Collectors.toList());
    }

    public List<UserMovieLabelDto> getRatingsForUser(Long userId) throws UserNotFoundException {
        if (!userRepo.existsById(userId)) {
            throw new UserNotFoundException("User not found for id " + userId);
        }

        List<UserMovieLabel> labels = ratingRepo.findByUserId(userId.toString());
        return labels.stream()
                .map(label -> mapper.map(label, UserMovieLabelDto.class))
                .collect(Collectors.toList());
    }

    public UserMovieLabel findByIds(UserMovieLabelDto userMovieLabelDto) throws RatingNotFoundException {
        Optional<UserMovieLabel> userMovieLabel = ratingRepo.findByUserIdAndMovieId(
                userMovieLabelDto.getUserId(), userMovieLabelDto.getMovieId());

        if (userMovieLabel.isEmpty()) {
            throw new RatingNotFoundException(UserMovieLabel.class, "movie/user_id");
        }

        return userMovieLabel.get();
    }

    public void createRating(UserMovieLabelDto userMovieLabelDto) {
        UserMovieLabel userMovieLabel = new UserMovieLabel();
        userMovieLabel.setLabel(userMovieLabelDto.getLabel());
        userMovieLabel.setUserId(userMovieLabelDto.getUserId());
        userMovieLabel.setMovieId(userMovieLabelDto.getMovieId());
        ratingRepo.save(userMovieLabel);
    }

    public boolean deleteRating(UserMovieLabelDto userMovieLabelDto) {
        Optional<UserMovieLabel> userMovieLabel = ratingRepo.findByUserIdAndMovieId(
                userMovieLabelDto.getUserId(), userMovieLabelDto.getMovieId());

        if (userMovieLabel.isEmpty()) {
            return false;
        }

        ratingRepo.deleteById(userMovieLabel.get().getId());
        return true;
    }

    public boolean updateRating(UserMovieLabelDto userMovieLabelDto) {
        Optional<UserMovieLabel> userMovieLabel = ratingRepo.findByUserIdAndMovieId(
                userMovieLabelDto.getUserId(), userMovieLabelDto.getMovieId());

        if (userMovieLabel.isEmpty()) {
            return false;
        }

        userMovieLabel.get().setLabel(userMovieLabelDto.getLabel());
        ratingRepo.save(userMovieLabel.get());
        return true;
    }
}
