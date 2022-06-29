package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Butterfly;

public interface ButterflyRepository extends MongoRepository<Butterfly, String> {
}
