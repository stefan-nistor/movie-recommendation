package ro.info.uaic.movierecommendation.exceptions;

public class EmailFormatException extends Exception{
    public EmailFormatException() {
        super("Invalid email address");
    }
}
