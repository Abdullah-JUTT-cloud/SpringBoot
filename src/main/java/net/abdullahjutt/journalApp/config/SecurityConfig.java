package net.abdullahjutt.journalApp.config;

import net.abdullahjutt.journalApp.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. We store the service here. No @Autowired annotation needed on the field.
    private final UserDetailsServiceImpl userDetailsService;

    // 2. Constructor: Spring automatically passes the UserDetailsServiceImpl here.
    // This breaks the circular dependency.
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 3. Bean for Password Encoder: Tells Spring how to encrypt/check passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 4. Bean for Authentication Provider: Connects your User Service with the Password Encoder.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 5. The Main Security Rules: What URLs are allowed?
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // A. Disable CSRF: Since we are building an API (not a website with forms), we don't need CSRF protection.
                .csrf(csrf -> csrf.disable())

                // B. Statelessness: Don't create sessions (cookies). Each request must carry its own credentials.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // C. Authorization Rules:
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/journal/**","/users/**").authenticated()// Anyone can access /public/...
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Only admins can access /admin/...
                        .anyRequest().authenticated()                   // Everything else requires login
                )

                // D. Login Method: Use HTTP Basic Auth (username/password in header) for now.
                .httpBasic(httpBasic -> httpBasic.realmName("JournalApp"));

        return http.build();
    }
}