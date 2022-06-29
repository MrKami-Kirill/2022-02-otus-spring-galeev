package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Caterpillar;

@Repository
public interface CaterpillarRepository extends MongoRepository<Caterpillar, String> {
}
