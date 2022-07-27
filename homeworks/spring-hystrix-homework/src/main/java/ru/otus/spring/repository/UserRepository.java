package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAll();

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    void deleteById(String id);

    void deleteByUsername(String username);
}
