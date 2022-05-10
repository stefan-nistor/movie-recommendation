package ro.info.uaic.movierecommendation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import ro.info.uaic.movierecommendation.dtoresponses.PasswordDTO;
import ro.info.uaic.movierecommendation.dtoresponses.UserDTO;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import ro.info.uaic.movierecommendation.exceptions.InvalidPasswordException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
import ro.info.uaic.movierecommendation.repos.UserRepo;

import java.util.Locale;
import java.util.UUID;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    public void sendTokenToEmail(String email) throws UserNotFoundException {
        var user = userRepo.findByEmail(email);
        if(user.isEmpty()) {
            throw new UserNotFoundException("No such user with given email");
        }
        // 1. generate token
        String token = UUID.randomUUID().toString();
        UserEntity userEntity = user.get();
        userEntity.setPasswordToken(token);
        userService.updateUser(new UserDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getLastname(), userEntity.getFirstname(), userEntity.getPasswordToken()));

        // 2. send email to email address

        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setFrom("movierecommendationa6@gmx.com");
        passwordResetEmail.setTo(userEntity.getEmail());
        passwordResetEmail.setSubject("Password Reset Request");
        passwordResetEmail.setText("To reset your password, click the link below:\n" + token  + "/reset?token=" + userEntity.getPasswordToken());

        emailService.sendEmail(passwordResetEmail);

    }

    @Override
    public void resetPassword(PasswordDTO password) throws InvalidPasswordException {

        //TODO Verify if token matches userToken

        /**
         * The password is at least 8 characters long (?=.{8,}).
         *
         * The password has at least one uppercase letter (?=.[A-Z]).
         *
         * The password has at least one lowercase letter (?=.[a-z]).
         *
         * The password has at least one digit (?=.*[0-9]).
         *
         * The password has at least one special character ([^A-Za-z0-9]).
         */


        if(!password.getNewPassword().matches("(?=.[a-z])(?=.[A-Z])(?=.[0-9])(?=.[^A-Za-z0-9])(?=.{8,})")) {
            throw new InvalidPasswordException("Must provide a stronger password");
        }

        if(!password.getNewPassword().equals(password.getConfirmPassword())) {
            throw new InvalidPasswordException("Confirmation password doesn't match");
        }


        var user = userRepo.findByPasswordToken(password.getResetToken());
        UserEntity userEntity = user.get();
        userEntity.setPassword(password.getNewPassword());

    }

}
