package me.bookstore.demo.controller;

import lombok.RequiredArgsConstructor;
import me.bookstore.demo.dto.AuthorDto;
import me.bookstore.demo.entity.Author;
import me.bookstore.demo.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/entity")
    public ResponseEntity<List<Author>> getAuthorsEntity() {
        List<Author> authors = authorService.getAllAuthorsEntity();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/")
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        List<AuthorDto> authors = authorService.getAllAuthors();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable UUID id) {
       return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> createAuthor(@RequestBody AuthorDto author) {
       return new ResponseEntity<>(authorService.createAuthor(author), HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<AuthorDto> updateAuthor(UUID authorId,@RequestBody AuthorDto author) {
       return ResponseEntity.ok(authorService.updateAuthor(authorId, author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@RequestBody UUID id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

}
