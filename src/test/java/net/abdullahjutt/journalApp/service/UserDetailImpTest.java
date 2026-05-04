package net.abdullahjutt.journalApp.service;

import net.abdullahjutt.journalApp.entity.User;
import net.abdullahjutt.journalApp.repository.UserRepository;
import net.abdullahjutt.journalApp.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailImpTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    @Disabled
    void loadByUsername() {
        User user = new User("abc", "dfsf");
        user.setRoles(List.of("USER"));

        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("Abdullah");

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("abc", userDetails.getUsername());
    }
}