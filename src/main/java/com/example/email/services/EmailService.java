package com.example.email.services;

import com.example.email.enums.StatusEmail;
import com.example.email.models.EmailModel;
import com.example.email.repositories.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final EmailRepository emailRepository;

    private final JavaMailSender emailSender;

    public void sendEmail(EmailModel emailModel) {
        if (emailModel.getEmailFrom() == null || emailModel.getEmailFrom().isEmpty()) {
            throw new InvalidParameterException("Email from cannot be null or empty");
        }
        if (emailModel.getEmailTo() == null || emailModel.getEmailTo().isEmpty()) {
            throw new InvalidParameterException("Email to cannot be null or empty");
        }
        if (emailModel.getSubject() == null || emailModel.getSubject().isEmpty()) {
            throw new InvalidParameterException("Subject cannot be null or empty");
        }
        if (emailModel.getText() == null || emailModel.getText().isEmpty()) {
            throw new InvalidParameterException("Message cannot be null or empty");
        }
        if (emailModel.getPhone() == null || emailModel.getPhone().isEmpty()) {
            throw new InvalidParameterException("Phone cannot be null or empty");
        }

        emailModel.setSendDateEmail(LocalDateTime.now());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText() + "\nPhone: " + emailModel.getPhone());
            emailSender.send(message);
            emailModel.setStatusEmail(StatusEmail.SENT);
            System.out.println("E-mail enviado");
        } catch (MailException e) {
            emailModel.setStatusEmail(StatusEmail.ERROR);
            System.out.println("Não foi possível enviar o e-mail: " + e.getMessage());
        } finally {
            emailRepository.save(emailModel);
        }
    }

    public Page<EmailModel> findAll(Pageable pageable) {
        return  emailRepository.findAll(pageable);
    }

    public Optional<EmailModel> findById(UUID emailId) { return emailRepository.findById(emailId);
    }
}
