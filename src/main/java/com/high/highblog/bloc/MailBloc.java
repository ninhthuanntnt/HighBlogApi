package com.high.highblog.bloc;

import com.high.highblog.constant.AppErrorCode;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.ConfirmationCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class MailBloc {

    private final JavaMailSender javaMailSender;

    private final String registrationEmailTemplate;

    private static final String CONFIRMATION_URL_PATTERN = "%s/%s?code=%s";

    public MailBloc(final JavaMailSender javaMailSender,
                    @Qualifier("registrationEmailTemplate") final String registrationEmailTemplate) {
        this.javaMailSender = javaMailSender;
        this.registrationEmailTemplate = registrationEmailTemplate;
    }

    @Async("taskExecutor")
    public void sendConfirmRegistrationMailTo(final String email,
                                              final String returnUrl,
                                              final ConfirmationCode confirmationCode) {
        log.info("Send mail to #{}", email);

        MimeMessage mimeMessage = buildMimeMessage(email,
                                                   "High blog confirm registration",
                                                   registrationEmailTemplate,
                                                   returnUrl,
                                                   confirmationCode);

        javaMailSender.send(mimeMessage);
    }

    private MimeMessage buildMimeMessage(final String email,
                                         final String subject,
                                         final String emailTemplate,
                                         final String returnUrl,
                                         final ConfirmationCode confirmationCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(String.format(emailTemplate,
                                                    generateConfirmationUrl(returnUrl, confirmationCode)),
                                      true);
        } catch (MessagingException e) {
            log.error("Error when build mime message");
            throw new ValidatorException("email", AppErrorCode.INVALID_EMAIL_INFO);
        }

        return mimeMessage;
    }

    private String generateConfirmationUrl(final String returnUrl, final ConfirmationCode confirmationCode) {
        return String.format(CONFIRMATION_URL_PATTERN,
                             returnUrl,
                             confirmationCode.getId(),
                             confirmationCode.getCode());
    }
}
