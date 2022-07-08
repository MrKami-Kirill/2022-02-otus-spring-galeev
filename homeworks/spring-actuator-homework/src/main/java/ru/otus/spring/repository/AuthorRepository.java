package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "authors", path = "authors")
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByName(String name);
}
