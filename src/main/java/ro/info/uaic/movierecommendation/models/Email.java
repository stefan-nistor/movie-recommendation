package ro.info.uaic.movierecommendation.models;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Email {
    String mailDomain;
    String mailName;


    public Email(String emailAddress) {

        isValidEmailAddress(emailAddress);

        String[] arrOfSubstrings = emailAddress.split("@", 2);

        mailName = arrOfSubstrings[0];
        mailDomain = arrOfSubstrings[1];

    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public String getMailAddress(){
        return mailName + "@" + mailDomain;
    }

    public String getMailDomain() {
        return mailDomain;
    }

    public void setMailDomain(String mailDomain) {
        this.mailDomain = mailDomain;
    }

    public String getMailName() {
        return mailName;
    }

    public void setMailName(String mailName) {
        this.mailName = mailName;
    }

    @Override
    public String toString() {
        return "Email{" +
                "mailDomain='" + mailDomain + '\'' +
                ", mailName='" + mailName + '\'' +
                '}';
    }
}
