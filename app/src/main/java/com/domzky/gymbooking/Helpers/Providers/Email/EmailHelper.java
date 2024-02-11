package com.domzky.gymbooking.Helpers.Providers.Email;

import com.domzky.gymbooking.Helpers.Providers.Email.EmailLibraries.GMailSender;

public class EmailHelper {

    public void emailSend(String subject,String body,String sender,String recipients) {
        String username = "";
        String password = "";
        try {
            new GMailSender(username,password).sendMail(subject,body,sender,recipients);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
