package ro.info.uaic.movierecommendation.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    private static final String MAIL_SUBJECT = "Reset password request";

    @Override
    public void sendEmail(String to, String message) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setSubject(MAIL_SUBJECT);
        mail.setTo(to);
        mail.setText(message);
        javaMailSender.send(mail);

    }

}