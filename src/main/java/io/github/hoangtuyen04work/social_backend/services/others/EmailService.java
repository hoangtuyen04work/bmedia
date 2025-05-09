package io.github.hoangtuyen04work.social_backend.services.others;

import jakarta.mail.MessagingException;

public interface EmailService {
    void signupVerify(String email, String code) throws MessagingException;

    void test() throws MessagingException;
}
