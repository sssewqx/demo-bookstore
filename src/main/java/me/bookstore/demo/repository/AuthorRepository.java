package me.bookstore.demo.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import me.bookstore.demo.entity.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Author a SET a.firstName = ?2, a.lastName = ?3 WHERE a.id = ?1")
    int updateAuthor(UUID id, String firstName, String lastName);

    @Modifying
    @Transactional
    @Query(value = """
                    DELETE FROM Book WHERE author_id = :authorId;
                    DELETE FROM Author WHERE id = :authorId
                    """, nativeQuery = true)
    int deleteByIdNativeQuery(@Param("authorId") UUID authorId);

    @EntityGraph(attributePaths = "books")
    List<Author> findAll();

    @EntityGraph(attributePaths = "books")
    Optional<Author> findById(UUID id);
}