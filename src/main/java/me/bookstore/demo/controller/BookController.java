package me.bookstore.demo.controller;

import lombok.RequiredArgsConstructor;
import me.bookstore.demo.dto.BookDto;
import me.bookstore.demo.dto.BookUpdateRequest;
import me.bookstore.demo.entity.Book;
import me.bookstore.demo.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping("/entity")
    public ResponseEntity<List<Book>> getAuthorsEntity() {
        List<Book> books = bookService.getAllBooksEntity();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable UUID id) {
       return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> createBook(@RequestBody BookDto book) {
       return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<BookUpdateRequest> updateBook(UUID id, @RequestBody BookUpdateRequest book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
