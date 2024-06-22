package com.test.tasks.service;

import com.test.tasks.model.EmailPojo;

public interface EmailService {
    String sendSimpleMail(EmailPojo details);

    String sendMailWithAttachment(EmailPojo details);
}
