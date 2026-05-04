package net.abdullahjutt.journalApp.repository;

import net.abdullahjutt.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository  extends MongoRepository<User, ObjectId> {
    public User findByUsername(String username);
    public User deleteByUsername(String username);
}
