package ro.info.uaic.movierecommendation.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.UserMovieRatingDto;
import ro.info.uaic.movierecommendation.entites.UserMovieRating;
import ro.info.uaic.movierecommendation.exceptions.MovieNotFoundException;
import ro.info.uaic.movierecommendation.exceptions.RatingNotFoundException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.repos.RatingRepository;
import ro.info.uaic.movierecommendation.repos.UserRepo;
import ro.info.uaic.movierecommendation.repositories.movies.MovieRepository;

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
    private MovieRepository movieRepo;

    @Autowired
    private ModelMapper mapper;

    public List<UserMovieRatingDto> getAllRatings() {
        return ratingRepo.findAll().stream()
                .map(rating -> mapper.map(rating, UserMovieRatingDto.class))
                .collect(Collectors.toList());
    }

    public List<UserMovieRatingDto> getRatingsForUser(Long userId) throws UserNotFoundException {
        if (!userRepo.existsById(userId)) {
            throw new UserNotFoundException("User not found for id " + userId);
        }

        List<UserMovieRating> ratings = ratingRepo.findByUserId(userId.toString());
        return ratings.stream()
                .map(rating -> mapper.map(rating, UserMovieRatingDto.class))
                .collect(Collectors.toList());
    }

    public List<UserMovieRatingDto> getRatingsForMovie(Long movieId) {
        if (!movieRepo.existsById(movieId)) {
            throw new MovieNotFoundException(Movie.class, "id");
        }

        List<UserMovieRating> ratings = ratingRepo.findByMovieId(movieId.toString());
        return ratings.stream()
                .map(rating -> mapper.map(rating, UserMovieRatingDto.class))
                .collect(Collectors.toList());
    }

    public UserMovieRating findByIds(Long userId, Long movieId) throws RatingNotFoundException {
        Optional<UserMovieRating> userMovieRating = ratingRepo.findByUserIdAndMovieId(
                userId.toString(), movieId.toString());

        if (userMovieRating.isEmpty()) {
            throw new RatingNotFoundException(UserMovieRating.class, "movie/user_id");
        }

        return userMovieRating.get();
    }

    public void createRating(UserMovieRatingDto userMovieRatingDto) {
        UserMovieRating userMovieRating = new UserMovieRating();
        userMovieRating.setLabel(userMovieRatingDto.getLabel());
        userMovieRating.setRating(userMovieRatingDto.getRating());
        userMovieRating.setUserId(userMovieRatingDto.getUserId());
        userMovieRating.setMovieId(userMovieRatingDto.getMovieId());

        userMovieRating.setLabel(userMovieRating.getRating() > 5);

        ratingRepo.save(userMovieRating);
    }

    public boolean deleteRating(Long userId, Long movieId) {
        Optional<UserMovieRating> userMovieRating = ratingRepo.findByUserIdAndMovieId(
                userId.toString(), movieId.toString());

        if (userMovieRating.isEmpty()) {
            return false;
        }

        ratingRepo.deleteById(userMovieRating.get().getId());
        return true;
    }

    public boolean updateRating(UserMovieRatingDto userMovieRatingDto) {
        Optional<UserMovieRating> userMovieRating = ratingRepo.findByUserIdAndMovieId(
                userMovieRatingDto.getUserId(), userMovieRatingDto.getMovieId());
        if (userMovieRating.isEmpty()) {
            return false;
        }

        if (userMovieRatingDto.getRating() != null) {
            userMovieRating.get().setRating(userMovieRatingDto.getRating());
        }
        if (userMovieRatingDto.getLabel() != null) {
            userMovieRating.get().setLabel(userMovieRatingDto.getLabel());
        }
        userMovieRating.get().setLabel(userMovieRating.get().getRating() > 5);

        ratingRepo.save(userMovieRating.get());
        return true;
    }
}
