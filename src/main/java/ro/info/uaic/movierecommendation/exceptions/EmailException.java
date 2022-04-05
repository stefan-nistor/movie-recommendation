package ro.info.uaic.movierecommendation.exceptions;

public class EmailException extends Exception{
    public EmailException() {
        super("Invalid eMail address");
    }
}
