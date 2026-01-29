package fr.elecmove.api.messaging;

import fr.elecmove.api.model.User;

public interface MailService {
    void sendEmailValidation(User user, String token);
    void sendResetPassword(User user, String token);
}
