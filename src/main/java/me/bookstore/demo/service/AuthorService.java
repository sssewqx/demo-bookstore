package me.bookstore.demo.service;

import lombok.RequiredArgsConstructor;
import me.bookstore.demo.dto.AuthorDto;
import me.bookstore.demo.entity.Author;
import me.bookstore.demo.mapper.AuthorMapper;
import me.bookstore.demo.repository.AuthorRepository;
import me.bookstore.demo.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;


    public List<Author> getAllAuthorsEntity() {
        return authorRepository.findAll();
    }

    public List<AuthorDto> getAllAuthors() {
        List<Author> authorsEntity = authorRepository.findAll();
        return authorMapper.authorToAuthorDto(authorsEntity);
    }

    public AuthorDto getAuthorById(UUID id) {
        Author authorEntity = authorRepository.findById(id)
                .orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found."));
        return authorMapper.authorToAuthorDto(authorEntity);
    }

    public UUID createAuthor(AuthorDto authorDto) {
        Author authorEntity = authorMapper.authorDtoToAuthor(authorDto, bookRepository, new Author());
        authorRepository.save(authorEntity);
        return authorEntity.getId();
    }

    public AuthorDto updateAuthor(UUID id, AuthorDto authorDto) {
        var author = authorRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found."));
                authorMapper.authorDtoToAuthor(authorDto, bookRepository, author);
                authorRepository.save(author);
                return authorMapper.authorToAuthorDto(author);
    }

    public void deleteAuthor(UUID id) {
        authorRepository.findById(id)
                .orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found."));
    }

}
