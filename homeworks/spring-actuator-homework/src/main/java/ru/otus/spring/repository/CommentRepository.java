package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Comment;

@Repository
@RepositoryRestResource(collectionResourceRel = "comments", path = "comments")
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
