package com.incture.cpm.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public String sendOtpEmail(String email, String otp) throws MessagingException {
       // Try block to check for exceptions
       try {

           // Creating a simple mail message
           SimpleMailMessage mailMessage = new SimpleMailMessage();

           // Setting up necessary details
           mailMessage.setFrom(sender);
           mailMessage.setTo(email);
            mailMessage.setText(otp);
           mailMessage.setSubject("Verify Otp");
//           mailMessage.setText(""");
           mailMessage.setText("OTP is " + otp);

           // Sending the mail
           System.out.println("yyy");
            javaMailSender.send(mailMessage);
            System.out.println("nnnnn");
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
}