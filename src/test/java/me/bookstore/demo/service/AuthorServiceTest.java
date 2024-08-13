package me.bookstore.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import me.bookstore.demo.dto.AuthorDto;
import me.bookstore.demo.dto.AuthorUpdateRequest;
import me.bookstore.demo.entity.Author;
import me.bookstore.demo.mapper.AuthorMapper;
import me.bookstore.demo.repository.AuthorRepository;
import me.bookstore.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDto authorDto;
    private UUID authorId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authorId = UUID.randomUUID();
        author = new Author();
        author.setId(authorId);
        author.setFirstName("John");
        author.setLastName("Doe");

        authorDto = new AuthorDto("John", "Doe", Collections.emptyList());
    }

    @Test
    @DisplayName("Получить всех авторов")
    public void testGetAllAuthors() {
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(author));
        when(authorMapper.authorToAuthorDto(anyList())).thenReturn(Collections.singletonList(authorDto));

        List<AuthorDto> authors = authorService.getAllAuthors();

        assertNotNull(authors);
        assertEquals(1, authors.size());
        verify(authorRepository, times(1)).findAll();
        verify(authorMapper, times(1)).authorToAuthorDto(anyList());
    }

    @Test
    @DisplayName("Получить автора по ID")
    public void testGetAuthorById() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDto);

        AuthorDto foundAuthor = authorService.getAuthorById(authorId);

        assertNotNull(foundAuthor);
        assertEquals("John", foundAuthor.firstName());
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorMapper, times(1)).authorToAuthorDto(author);
    }

    @Test
    @DisplayName("Exception если автор небыл найден по ID")
    public void testGetAuthorByIdNotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authorService.getAuthorById(authorId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    @DisplayName("Создать автора")
    public void testCreateAuthor() {
        when(authorMapper.authorDtoToAuthor(any(AuthorDto.class), eq(bookRepository), any(Author.class))).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);

        UUID savedAuthorId = authorService.createAuthor(authorDto);

        assertNotNull(savedAuthorId);
        assertEquals(authorId, savedAuthorId);
        verify(authorMapper, times(1)).authorDtoToAuthor(any(AuthorDto.class), eq(bookRepository), any(Author.class));
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    @DisplayName("Обновить автора")
    public void testUpdateAuthor() {
        when(authorRepository.updateAuthor(authorId, "John", "Doe")).thenReturn(1);

        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest("John", "Doe");
        AuthorUpdateRequest updatedAuthor = authorService.updateAuthor(authorId, updateRequest);

        assertNotNull(updatedAuthor);
        assertEquals("John", updatedAuthor.firstName());
        verify(authorRepository, times(1)).updateAuthor(authorId, "John", "Doe");
    }

    @Test
    @DisplayName("Exception если обновляемый автор не найден")
    public void testUpdateAuthorNotFound() {
        when(authorRepository.updateAuthor(authorId, "John", "Doe")).thenReturn(0);

        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest("John", "Doe");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authorService.updateAuthor(authorId, updateRequest);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(authorRepository, times(1)).updateAuthor(authorId, "John", "Doe");
    }

    @Test
    @DisplayName("Удалить автора")
    public void testDeleteAuthor() {
        when(authorRepository.deleteAuthorById(authorId)).thenReturn(1);

        authorService.deleteAuthor(authorId);

        verify(authorRepository, times(1)).deleteAuthorById(authorId);
    }

    @Test
    @DisplayName("Exception если удаляемый автор не найден")
    public void testDeleteAuthorNotFound() {
        when(authorRepository.deleteAuthorById(authorId)).thenReturn(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authorService.deleteAuthor(authorId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(authorRepository, times(1)).deleteAuthorById(authorId);
    }
}