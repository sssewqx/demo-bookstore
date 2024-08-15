package me.bookstore.demo.controller;

import lombok.RequiredArgsConstructor;
import me.bookstore.demo.dto.AuthorDto;
import me.bookstore.demo.dto.AuthorUpdateRequest;
import me.bookstore.demo.entity.Author;
import me.bookstore.demo.service.AuthorService;
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
    public ResponseEntity<AuthorUpdateRequest> updateAuthor(UUID authorId, @RequestBody AuthorUpdateRequest author) {
       return ResponseEntity.ok(authorService.updateAuthor(authorId, author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
