package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Butterfly;

@Repository
public interface ButterflyRepository extends MongoRepository<Butterfly, String> {
}
