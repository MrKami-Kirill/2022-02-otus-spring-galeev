package ru.otus.spring.repository.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.model.h2.BookH2;

@Repository
public interface BookH2Repository extends JpaRepository<BookH2, Long> {
}
