package net.abdullahjutt.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.abdullahjutt.journalApp.entity.User;
import net.abdullahjutt.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    public void saveUser(User user){
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Err occured for {}",user.getUsername(),e);
            throw new RuntimeException(e);
        }
    }
    public boolean saveNewUser(User user){
        try {
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
//            throw new RuntimeException(e);
            log.error("Err occured for {}",user.getUsername(),e);
            return false;
        }
    }
    public void createAdmin(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER","ADMIN"));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<User> getAll(){
        return  userRepository.findAll();
    }
    public Optional<User> findById(ObjectId Id){
        return userRepository.findById(Id);
    }
    public void deleteById(ObjectId Id){
        userRepository.deleteById(Id);
    }
    public  User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
