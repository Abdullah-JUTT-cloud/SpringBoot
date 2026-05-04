package net.abdullahjutt.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.abdullahjutt.journalApp.entity.JournalEntry;
import net.abdullahjutt.journalApp.entity.User;
import net.abdullahjutt.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;


    @Transactional
    public void saveEntry(JournalEntry journalEntry,String username){
        try {
            User user=userService.findByUsername(username);
            if(user == null){
                throw new RuntimeException("User not found");
            }
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);

            user.getJournalEntryListOfUsers().add(saved);

            userService.saveUser(user);
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occuured while saving ...",e);
        }

    }
    public void saveEntry(JournalEntry journalEntry){
        try {

           journalEntryRepository.save(journalEntry);
        }catch (Exception e){
            log.error("Ecexption",e);
        }

    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId Id){
        return journalEntryRepository.findById(Id);
    }
    @Transactional
    public boolean deleteById(ObjectId Id, String username) {
        boolean removed=false;
        try {
            User user = userService.findByUsername(username);
             removed= user.getJournalEntryListOfUsers().removeIf(x -> x.getId().equals(Id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(Id);
            }
        }catch (Exception e){
            log.error("Error",e);
            throw new RuntimeException("An error occured while deleting ...",e);
        }
        return removed;
    }
    public List<JournalEntry> findByUsername(String username){
        User user = userService.findByUsername(username);
        return user != null ? user.getJournalEntryListOfUsers() : List.of();
    }
}
