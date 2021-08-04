package springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springboot.models.DTO.NotificationDTO;
import springboot.models.DTO.PostDTO;
import springboot.models.DTO.PostEmployeeDTO;
import springboot.models.Post;
import springboot.models.PostEmployee;
import springboot.models.User;
import springboot.services.NotificationService;
import springboot.services.base.PostEmployeeService;
import springboot.services.base.PostService;
import springboot.services.base.UserService;

import javax.mail.*;
import javax.mail.internet.*;
import javax.validation.Valid;
import java.util.Properties;

@Controller
public class EmailController {


    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PostEmployeeService postEmployeeService;

    @Autowired
    private UserService userService;

    @ModelAttribute("mail")
    public NotificationDTO mailDto() {
        return new NotificationDTO();
    }

    @PostMapping("/sendemail/{id}")
    public String sendEmail(@ModelAttribute("mail") @Valid NotificationDTO notificationDTO) {
            notificationService.sendNotification(notificationDTO);
        return "redirect:/index";
    }

    @GetMapping("/write/email/{id}")
    public String writeEmail(@PathVariable("id") String post_id, Model model) {
        model.addAttribute("post_id", post_id);
        return "write_email";
    }

    private void sendEmailExample() {
        try {
        // Recipient's email ID needs to be mentioned.
        String to = "computerproject1821@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "computerproject1821@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // Set Subject: header field
        message.setSubject("This is the Subject Line!");

        // Now set the actual message
        message.setText("This is actual message");

        // Send message
        Transport.send(message);
        System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
        mex.printStackTrace();
        }

    }

}