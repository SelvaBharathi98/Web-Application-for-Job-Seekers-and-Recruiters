package springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import springboot.models.DTO.NotificationDTO;
import springboot.models.User;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendNotification(NotificationDTO notificationDTO) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("bharathiselvam.sb20@gmail.com");
        mail.setFrom("computerproject1821@gmail.com");
        mail.setSubject(notificationDTO.getSubject());
        mail.setText(notificationDTO.getFirstname() + " " + notificationDTO.getLastname() + ", " + notificationDTO.getMessage());

        javaMailSender.send(mail);
    }
    public void sendApprovedNotification(NotificationDTO notificationDTO) throws MailException{
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("bharathiselvam.sb20@gmail.com");
        mail.setTo("bharathiselvam.sb20@gmail.com");
        mail.setFrom("computerproject1821@gmail.com");
        mail.setSubject(notificationDTO.getSubject());
        mail.setText("You are approved! Check your account!");

        javaMailSender.send(mail);
    }

    public void sendRegistrationNotification(NotificationDTO notificationDTO) throws MailException{
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("bharathiselvam.sb20@gmail.com");
        mail.setFrom("computerproject1821@gmail.com");
        mail.setSubject("Registration");
        mail.setText("You are successfully registered! Check your account!");

        javaMailSender.send(mail);
    }
}
