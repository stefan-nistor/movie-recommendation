package ro.info.uaic.movierecommendation.exceptions;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
