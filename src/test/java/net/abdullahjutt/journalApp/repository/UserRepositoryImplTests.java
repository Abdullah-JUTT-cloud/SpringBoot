package net.abdullahjutt.journalApp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTests {
    @Autowired
    private UserRepositoryImpl userRepositoryImp;
    @Test
    public void getSAUsers(){
        Assertions.assertNotNull(userRepositoryImp.getUsersForSA());
    }
}
