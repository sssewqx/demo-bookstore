package me.bookstore.demo.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.bookstore.demo.AbstractIntegrationTest;
import me.bookstore.demo.dto.BookDto;
import me.bookstore.demo.dto.BookUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookServiceTest extends AbstractIntegrationTest {

    private final BookService bookService;

    private final UUID BOOK_ID = UUID.fromString("2e79d637-a309-43c0-a59e-7018985f612f");
    private final UUID AUTHOR_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final UUID INVALID_BOOK_ID = UUID.fromString("838f1727-36ca-4f51-bf8b-4d2bf59300be");

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
    @DisplayName("Exception если книга не найдена")
    void testGetBookByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(INVALID_BOOK_ID));
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
    @DisplayName("Exception если не найдена книга для обновления")
    void testUpdateBookNotFound() {
        BookUpdateRequest updateRequest = new BookUpdateRequest("New Title");
        assertThrows(EntityNotFoundException.class, ()
                -> bookService.updateBook(INVALID_BOOK_ID, updateRequest));
    }

    @Test
    @DisplayName("Удалить книгу")
    public void testDeleteBook() {
        bookService.deleteBook(BOOK_ID);
        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(BOOK_ID));
    }

}