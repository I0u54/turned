package com.example.daret.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class ReminderMailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String to, String subject, String templateName, Context context) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);

            helper.setTo(to);
            helper.setSubject(subject);

            String emailContent = templateEngine.process(templateName, context);

            helper.setText(emailContent, true);

            javaMailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
