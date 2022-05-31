package ro.info.uaic.movierecommendation.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.PasswordDTO;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.InvalidPasswordException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
import ro.info.uaic.movierecommendation.repos.UserRepo;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void sendTokenToEmail(String email) {
        var user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No such user with given email");
        }
        String token = UUID.randomUUID().toString();
        UserEntity userEntity = user.get();
        userEntity.setPasswordToken(token);
        userService.updateUser(mapper.map(userEntity, UserDTO.class));

        String text = "To reset your password, click the link below:\nhttps://a6-movie-recommendation.netlify.app/reset-pass?token=" + userEntity.getPasswordToken();

        emailService.sendEmail(user.get().getEmail(), text);
    }

    @Override
    @Transactional
    public void resetPassword(PasswordDTO password) {

        /**
         * The password is at least 6 characters long (?=.{8,}).
         *
         * The password has at least one uppercase letter (?=.[A-Z]).
         *
         * The password has at least one lowercase letter (?=.[a-z]).
         *
         * The password has at least one special character ([^A-Za-z0-9]).
         */


        if (!password.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–/|{}\\\\:;'\\-%`€\"_.,?*~$^+=<>\\]\\[]).{6,}$")) {
            throw new InvalidPasswordException("Must provide a stronger password");
        }

        if (!password.getNewPassword().equals(password.getConfirmPassword())) {
            throw new InvalidPasswordException("Confirmation password doesn't match");
        }

        var userId = userRepo.findByPasswordToken(password.getResetToken()).orElseThrow(() -> new UserNotFoundException("Bad reset token"));
        var user = userRepo.findById(userId.getId()).orElseThrow(() -> new UserNotFoundException("user not found"));
        String encryptedPassword = passwordEncoder.encode(password.getNewPassword());
        user.setPassword(encryptedPassword);
        user.setPasswordToken(null);
        userRepo.save(user);
    }

}
