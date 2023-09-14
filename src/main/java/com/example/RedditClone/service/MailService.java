package com.example.RedditClone.service;

import com.example.RedditClone.exceptions.SpringRedditException;
import com.example.RedditClone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    public void sendMail(NotificationEmail notificationEmail){

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("state_octavian@yahoo.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try{
            mailSender.send(messagePreparator);
            log.info("Activation email sent!");

        } catch (MailException e){
            log.error("Exception occurred when sending mail", e);
            throw  new SpringRedditException("Exception occurred when sending mail to  " + notificationEmail.getRecipient(), e);
        }
    }

    @Async
    public void sendMailNoTrapMail(String toEmail,
                                   String subject,
                                   String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("stateoctavian22@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        try{
            mailSender.send(message);
            log.info("Activation email sent!");

        } catch (MailException e){
            log.error("Exception occurred when sending mail", e);
            throw  new SpringRedditException("Exception occurred when sending mail to  " + toEmail, e);
        }



    }
}
