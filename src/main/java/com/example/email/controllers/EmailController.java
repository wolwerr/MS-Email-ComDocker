package com.example.email.controllers;


import com.example.email.models.EmailModel;
import com.example.email.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sending-email")
    public ResponseEntity<EmailModel> sendingEmail(String emailFrom, String emailTo, String phone, String subject, String message) {
        EmailModel emailModel = new EmailModel();
        emailModel.setEmailFrom(emailFrom);
        emailModel.setEmailTo(emailTo);
        emailModel.setPhone(phone);
        emailModel.setSubject(subject);
        emailModel.setText(message);
        emailService.sendEmail(emailModel);
        return ResponseEntity.ok(emailModel);
    }

    @GetMapping("/emails")
    public ResponseEntity<Page<EmailModel>> getAllEmails(@PageableDefault(page = 0, size = 5) Pageable pageable){
        return new ResponseEntity<>(emailService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/emails/{emailId}")
    public ResponseEntity<Object> getOneEmail(@PathVariable(value="emailId") UUID emailId){
        Optional<EmailModel> emailModelOptional = emailService.findById(emailId);
        return emailModelOptional.<ResponseEntity<Object>>map(emailModel -> ResponseEntity.status(HttpStatus.OK).body(emailModel)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found."));
    }
}
