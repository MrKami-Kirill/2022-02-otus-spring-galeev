package ru.otus.spring.repository.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring.model.h2.GenreH2;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GenreH2Repository extends JpaRepository<GenreH2, Long> {

    Optional<GenreH2> findByName(String name);

    @Query(value = "SELECT g FROM GenreH2 g " +
            "WHERE g.name IN :nameList")
    Set<GenreH2> findAllGenresByNameList(@Param("nameList") List<String> nameList);
}
