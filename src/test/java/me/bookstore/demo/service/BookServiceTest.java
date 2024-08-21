package me.bookstore.demo.service;

import lombok.RequiredArgsConstructor;
import me.bookstore.demo.AbstractIntegrationTest;
import me.bookstore.demo.dto.BookDto;
import me.bookstore.demo.dto.BookUpdateRequest;
import me.bookstore.demo.exception.BookNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookServiceTest extends AbstractIntegrationTest {

    private final BookService bookService;

    private final UUID BOOK_ID = fromString("2e79d637-a309-43c0-a59e-7018985f612f");
    private final UUID AUTHOR_ID = fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID INVALID_BOOK_ID = fromString("838f1727-36ca-4f51-bf8b-4d2bf59300be");

    @Test
    @DisplayName("Получить все книги")
    public void testGetAllBooks() {
        List<BookDto> bookDtos = bookService.getAllBooks();

        assertNotNull(bookDtos);
    }

    @Test
    @DisplayName("Получить книгу по ID")
    public void testGetBookById() {
        BookDto bookDto = bookService.getBookById(BOOK_ID);

        assertNotNull(bookDto);
    }

    @Test
    @DisplayName("Найти несуществующую книгу по ID")
    void testGetBookByIdNotFound() {
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(INVALID_BOOK_ID));
    }

    @Test
    @DisplayName("Создать книгу")
    public void testCreateBook() {
        BookDto bookDto = new BookDto("New Book", AUTHOR_ID);

        UUID bookId = bookService.createBook(bookDto);

        assertNotNull(bookId);
    }

    @Test
    @DisplayName("Обновить книгу")
    public void testUpdateBook() {
        BookUpdateRequest updateRequest = new BookUpdateRequest("Updated Title");
        BookUpdateRequest updatedBook = bookService.updateBook(BOOK_ID, updateRequest);

        assertEquals("Updated Title", updatedBook.title());
    }

    @Test
    @DisplayName("Обновить несуществующую книгу")
    void testUpdateBookNotFound() {
        BookUpdateRequest updateRequest = new BookUpdateRequest("New Title");

        assertThrows(BookNotFoundException.class, ()
                -> bookService.updateBook(INVALID_BOOK_ID, updateRequest));
    }

    @Test
    @DisplayName("Удалить книгу")
    public void testDeleteBook() {
        bookService.deleteBook(BOOK_ID);

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(BOOK_ID));
    }

    @Test
    @DisplayName("Удалить несуществующую книгу")
    public void testDeleteBookNotFound() {
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(INVALID_BOOK_ID));
    }
}