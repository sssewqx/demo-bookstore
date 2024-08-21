package me.bookstore.demo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.bookstore.demo.AbstractIntegrationTest;
import me.bookstore.demo.dto.AuthorDto;
import me.bookstore.demo.dto.AuthorUpdateRequest;
import me.bookstore.demo.exception.AuthorNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorServiceTest extends AbstractIntegrationTest {

    private final AuthorService authorService;

    private final UUID AUTHOR_ID = fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID INVALID_AUTHOR_ID = fromString("838f1727-36ca-4f51-bf8b-4d2bf59300be");

    @Test
    @DisplayName("Получить всех авторов")
    public void testGetAllAuthors() {
        List<AuthorDto> authorDtos = authorService.getAllAuthors();

        assertNotNull(authorDtos);
    }

    @Test
    @DisplayName("Получить автора по ID")
    public void testGetAuthorById() {
        AuthorDto authorDto = authorService.getAuthorById(AUTHOR_ID);

        assertNotNull(authorDto);
        assertNotNull(authorDto.booksId());
    }

    @Test
    @DisplayName("Найти несуществующего автора по IP")
    public void testGetAuthorByIdNotFound() {
        assertThrows(AuthorNotFoundException.class, () -> authorService.getAuthorById(INVALID_AUTHOR_ID));
    }

    @Test
    @DisplayName("Создать автора")
    public void testCreateAuthor() {
        AuthorDto authorDto = new AuthorDto("John", "Doe", List.of(UUID.randomUUID()));

        UUID authorId = authorService.createAuthor(authorDto);

        assertNotNull(authorId);
    }

    @Test
    @DisplayName("Обновить автора")
    public void testUpdateAuthor() {
        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest("Jane", "Doe");
        AuthorUpdateRequest updatedAuthor = authorService.updateAuthor(AUTHOR_ID, updateRequest);

        assertEquals("Jane", updatedAuthor.firstName());
        assertEquals("Doe", updatedAuthor.lastName());
    }

    @Test
    @DisplayName("Обновить несуществующего автора")
    public void testUpdateAuthorNotFound() {
        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest("Jane", "Doe");

        assertThrows(AuthorNotFoundException.class, ()
                -> authorService.updateAuthor(INVALID_AUTHOR_ID, updateRequest));
    }

    @Test
    @DisplayName("Удалить автора")
    public void testDeleteAuthor() {
        authorService.deleteAuthor(AUTHOR_ID);

        assertThrows(AuthorNotFoundException.class, () -> authorService.getAuthorById(AUTHOR_ID));
    }

    @Test
    @DisplayName("Удалить несуществующего автора")
    public void testDeleteAuthorNotFound() {
        assertThrows(AuthorNotFoundException.class, () -> authorService.deleteAuthor(INVALID_AUTHOR_ID));
    }
}