package net.abdullahjutt.journalApp.controller;

import net.abdullahjutt.journalApp.entity.User;
import net.abdullahjutt.journalApp.repository.UserRepository;
import net.abdullahjutt.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
        @Autowired
        private UserService userService;
        @Autowired
        private UserRepository userRepository;
//        @GetMapping
//        public ResponseEntity<?> getAll(){
//            List<User> userList= userService.getAll();
//            if(userList!=null && !userList.isEmpty()){
//                return new ResponseEntity<>(userList, HttpStatus.OK);
//            }
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//
//        }

        @GetMapping("/id/{id}")
        public ResponseEntity<User> getById(@PathVariable ObjectId id){
            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        @DeleteMapping
        public ResponseEntity<?> deleteById(){
                Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
                userRepository.deleteByUsername(authentication.getName());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        @PutMapping
        public ResponseEntity<?> updateUser(@RequestBody User currUser){
             try {
                 Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
                 String username=authentication.getName();
                 User oldUser = userService.findByUsername(username);
                     oldUser.setPassword(currUser.getPassword());
                     oldUser.setUsername(currUser.getUsername());
                     userService.saveNewUser(oldUser);
                     return new ResponseEntity<>(oldUser, HttpStatus.OK);
//                 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
             } catch (Exception e) {
                 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
             }

        }

}
