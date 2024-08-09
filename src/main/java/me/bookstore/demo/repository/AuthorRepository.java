package me.bookstore.demo.repository;

import jakarta.transaction.Transactional;
import me.bookstore.demo.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Author a SET a.firstName = ?2, a.lastName = ?3 WHERE a.id = ?1")
    int updateAuthor(UUID id, String firstName, String lastName);

    @Modifying
    @Transactional
    @Query("DELETE FROM Author a WHERE a.id = :id")
    int deleteAuthorById(UUID id);
}