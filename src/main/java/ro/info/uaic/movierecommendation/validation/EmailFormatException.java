package ro.info.uaic.movierecommendation.validation;

public class EmailFormatException extends Exception{
    public EmailFormatException() {
        super("Invalid email address");
    }
}
