package ru.otus.spring.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.model.mongo.BookMongo;

@Repository
public interface BookMongoRepository extends MongoRepository<BookMongo, String> {
}
