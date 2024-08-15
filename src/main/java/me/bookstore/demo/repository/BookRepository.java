package me.bookstore.demo.repository;

import jakarta.transaction.Transactional;
import me.bookstore.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.title = ?2 WHERE b.id = ?1")
    int updateBook(UUID id, String title);

    @Modifying
    @Transactional
    @Query("DELETE FROM Book b WHERE b.id = :id")
    int deleteByIdNativeQuery(UUID id);
}
