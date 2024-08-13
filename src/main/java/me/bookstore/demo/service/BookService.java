package me.bookstore.demo.service;

import lombok.RequiredArgsConstructor;
import me.bookstore.demo.dto.BookDto;
import me.bookstore.demo.dto.BookUpdateRequest;
import me.bookstore.demo.entity.Book;
import me.bookstore.demo.mapper.BookMapper;
import me.bookstore.demo.repository.AuthorRepository;
import me.bookstore.demo.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public List<Book> getAllBooksEntity() {
        return bookRepository.findAll();
    }
    public List<BookDto> getAllBooks() {
        List<Book> booksEntity = bookRepository.findAll();
        return bookMapper.bookToBookDto(booksEntity);
    }

    public BookDto getBookById(UUID id) {
        Book bookEntity = bookRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found."));
        return bookMapper.bookToBookDto(bookEntity);
    }

    public UUID createBook(BookDto bookDto) {
        Book bookEntity = bookMapper.bookDtoToBook(bookDto, authorRepository, new Book());
        var savedBook = bookRepository.save(bookEntity);
        return savedBook.getId();
    }

    public BookUpdateRequest updateBook(UUID id, BookUpdateRequest book) {
        if (bookRepository.updateBook(id, book.title()) == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found.");
        return book;
    }

    public void deleteBook(UUID id) {
       if (bookRepository.deleteBookById(id) == 0)
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found.");
    }
}
