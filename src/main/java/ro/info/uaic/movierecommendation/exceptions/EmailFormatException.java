package ro.info.uaic.movierecommendation.exceptions;

public class EmailFormatException extends RuntimeException{
    public EmailFormatException() {
        super("Invalid email address");
    }
}
