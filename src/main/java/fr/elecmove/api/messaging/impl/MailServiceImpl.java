package fr.elecmove.api.messaging.impl;

import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    private void sendMailBase(String to, String message, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(to);
            helper.setFrom("admin@elecmove.fr");
            helper.setSubject(subject);

            helper.setText(message,true); //Temporaire, email à remplacer par un JWT
            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException("Unable to send mail", e);
        }
    }

    @Override
    public void sendEmailValidation(User user, String token) {
        String serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String message = """
                    To validate your account click on <a href="%s">this link</a>
                    """
                .formatted(serverUrl+"/api/account/validate/"+token);
        sendMailBase(user.getEmail(), message, "Elecmove Email Validation");
    }

    @Override
    public void sendResetPassword(User user, String token) {
        String serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String message = """
                    To reset your password click on <a href="%s">this link</a>
                    """
                .formatted(serverUrl+"/reset-password.html?token="+token);
        sendMailBase(user.getEmail(), message, "Elecmove Reset Password");
    }


}
