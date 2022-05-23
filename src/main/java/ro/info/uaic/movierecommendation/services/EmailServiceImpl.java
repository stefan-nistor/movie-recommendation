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

//        MimeMessage msg = new MimeMessage(session);
//        Multipart multipart = new MimeMultipart();
//        BodyPart messageBodyPart = new MimeBodyPart();
//
//        try {
//            msg.setFrom(new InternetAddress(noReply));
//            msg.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(recipientsList == null ? mailRecipients : String.join(",", recipientsList)));
//            messageBodyPart.setContent(message, "text/html; charset=utf-8");
//            multipart.addBodyPart(messageBodyPart);
//
//            msg.setContent(multipart);
//            Transport.send(msg);
//            log.info("Email has been sent");
//            return true;
//        } catch (MessagingException e) {
//            log.error("Error at sending email: {}", e.getMessage());
//            return false;
//        }
    }

}