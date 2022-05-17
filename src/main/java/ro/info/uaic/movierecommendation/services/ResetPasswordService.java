package ro.info.uaic.movierecommendation.services;

import ro.info.uaic.movierecommendation.dtoresponses.PasswordDTO;
import ro.info.uaic.movierecommendation.exceptions.InvalidPasswordException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;

public interface ResetPasswordService {

    void sendTokenToEmail(String email) throws UserNotFoundException;

    void resetPassword(PasswordDTO password) throws InvalidPasswordException;
}
