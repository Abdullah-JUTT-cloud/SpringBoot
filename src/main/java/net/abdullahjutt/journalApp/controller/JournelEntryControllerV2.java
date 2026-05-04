package net.abdullahjutt.journalApp.controller;

import com.sun.net.httpserver.HttpsParameters;
import net.abdullahjutt.journalApp.entity.JournalEntry;
import net.abdullahjutt.journalApp.entity.User;
import net.abdullahjutt.journalApp.service.JournalEntryService;
import net.abdullahjutt.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournelEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllEnteriesOfUSers(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUsername(username);
        List<JournalEntry> journalEntryList= user.getJournalEntryListOfUsers();
        if(journalEntryList!=null && !journalEntryList.isEmpty()){
            return new ResponseEntity<>(journalEntryList,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        try {
//            myEntry.setDate(LocalDateTime.now());
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String username=authentication.getName();
            journalEntryService.saveEntry(myEntry,username);
             return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId myId){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user =userService.findByUsername(username);
        List<JournalEntry> collect=user.getJournalEntryListOfUsers().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(! collect.isEmpty()){

            Optional<JournalEntry> journalEntry= journalEntryService.findById(myId);
            return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId  myId){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
       boolean removed= journalEntryService.deleteById(myId,username);
       if(removed)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       else
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateById(@PathVariable ObjectId myId,@RequestBody JournalEntry newEntry){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
//        JournalEntry oldEntry=journalEntryService.findById(myId).orElse(null);
        User user=userService.findByUsername(username);
        List<JournalEntry> collect=user.getJournalEntryListOfUsers().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry= journalEntryService.findById(myId);
            if (journalEntry.isEmpty()) {
                return new ResponseEntity<>(newEntry, HttpStatus.NOT_FOUND);
            }

            JournalEntry oldEntry= journalEntry.get();
            oldEntry.setTitle(newEntry.getTitle()!=null &&  !newEntry.getTitle().isEmpty() ?newEntry.getTitle():oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent()!=null && !newEntry.getContent().isEmpty() ? newEntry.getContent():oldEntry.getContent());
            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry,HttpStatus.OK);
        }

        return new ResponseEntity<>(newEntry,HttpStatus.NOT_FOUND);

    }

}
