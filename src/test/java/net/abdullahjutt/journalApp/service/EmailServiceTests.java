package net.abdullahjutt.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {
    @Autowired
    private EmailService emailService;
    @Test
    public void testEmail(){
        emailService.sendEmail("abdullahjuttjutt910@gmail.com","java testing","hi hello");
    }
}
