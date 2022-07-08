package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "genres", path = "genres")
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);
}
