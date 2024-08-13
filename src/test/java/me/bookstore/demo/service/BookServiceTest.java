package me.bookstore.demo.service;

import me.bookstore.demo.dto.BookDto;
import me.bookstore.demo.dto.BookUpdateRequest;
import me.bookstore.demo.entity.Book;
import me.bookstore.demo.entity.Author;
import me.bookstore.demo.mapper.BookMapper;
import me.bookstore.demo.repository.AuthorRepository;
import me.bookstore.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Spy
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDto bookDto;
    private UUID bookId;
    private Author author;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        author = new Author(authorId, "John", "Doe", Collections.emptyList());
        book = new Book(bookId, "Test Title", author);
        bookDto = new BookDto("Test Title", authorId);
    }

    @Test
    @DisplayName("Получить все книги")
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));
        when(bookMapper.bookToBookDto(anyList())).thenReturn(Collections.singletonList(bookDto));

        List<BookDto> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(1, books.size());
        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).bookToBookDto(anyList());
    }

    @Test
    @DisplayName("Получить книгу по ID")
    void testGetBookById() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.bookToBookDto(book)).thenReturn(bookDto);

        BookDto foundBook = bookService.getBookById(bookId);

        assertNotNull(foundBook);
        assertEquals(bookDto.title(), foundBook.title());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).bookToBookDto(book);
    }

    @Test
    @DisplayName("Exception если книга не найдена")
    void testGetBookByIdNotFound() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> bookService.getBookById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    @DisplayName("Создать книгу")
    void testCreateBook() {

        when(authorRepository.findById(bookDto.authorId())).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        UUID createdBookId = bookService.createBook(bookDto);

        assertNotNull(createdBookId, "Идентификатор созданной книги не должен быть null");
        assertEquals(book.getId(), createdBookId, "Идентификатор созданной книги должен совпадать с ожидаемым");

        verify(authorRepository, times(1)).findById(bookDto.authorId());
        verify(bookRepository, times(1)).save(any(Book.class));
    }
    @Test
    @DisplayName("Обновить книгу")
    void testUpdateBook() {
        when(bookRepository.updateBook(bookId, bookDto.title())).thenReturn(1);

        BookUpdateRequest updatedBook = bookService.updateBook(bookId, new BookUpdateRequest(bookDto.title()));

        assertNotNull(updatedBook);
        assertEquals(bookDto.title(), updatedBook.title());
        verify(bookRepository, times(1)).updateBook(bookId, bookDto.title());
    }

    @Test
    @DisplayName("Exception если не найдена книга для обновления")
    void testUpdateBookNotFound() {
        when(bookRepository.updateBook(bookId, bookDto.title())).thenReturn(0);

        assertThrows(ResponseStatusException.class, () -> bookService.updateBook(bookId, new BookUpdateRequest(bookDto.title())));
        verify(bookRepository, times(1)).updateBook(bookId, bookDto.title());
    }

    @Test
    @DisplayName("Удалить книгу")
    void testDeleteBook() {
        when(bookRepository.deleteBookById(bookId)).thenReturn(1);

        assertDoesNotThrow(() -> bookService.deleteBook(bookId));
        verify(bookRepository, times(1)).deleteBookById(bookId);
    }

    @Test
    @DisplayName("Exception если удаляемая книга не найдена")
    void testDeleteBookNotFound() {
        when(bookRepository.deleteBookById(bookId)).thenReturn(0);

        assertThrows(ResponseStatusException.class, () -> bookService.deleteBook(bookId));
        verify(bookRepository, times(1)).deleteBookById(bookId);
    }
}