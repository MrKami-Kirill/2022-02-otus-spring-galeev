package ru.otus.spring.repository.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring.model.h2.AuthorH2;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorH2Repository extends JpaRepository<AuthorH2, Long> {

    Optional<AuthorH2> findByName(String name);

    @Query(value = "SELECT a FROM AuthorH2 a " +
            "WHERE a.name IN :nameList")
    Set<AuthorH2> findAllAuthorsByNameList(@Param("nameList") List<String> nameList);
}
