package me.bookstore.demo.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.bookstore.demo.dto.AuthorDto;
import me.bookstore.demo.dto.AuthorUpdateRequest;
import me.bookstore.demo.entity.Author;
import me.bookstore.demo.mapper.AuthorMapper;
import me.bookstore.demo.repository.AuthorRepository;
import me.bookstore.demo.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;

    private static final int DELETE_FAILED = 0;
    private static final int UPDATE_FAILED = 0;


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
                        -> new EntityNotFoundException("Author not found."));
        return authorMapper.authorToAuthorDto(authorEntity);
    }

    public UUID createAuthor(AuthorDto authorDto) {
        Author authorEntity = authorMapper.authorDtoToAuthor(authorDto, bookRepository, new Author());
        var savedAuthor =  authorRepository.save(authorEntity);
        return savedAuthor.getId();
    }

    public AuthorUpdateRequest updateAuthor(UUID id, AuthorUpdateRequest author) {
      if (authorRepository.updateAuthor(id, author.firstName(), author.lastName()) == UPDATE_FAILED)
          throw new EntityNotFoundException("Author not found.");
      return author;
    }

    public void deleteAuthor(UUID id) {
        if (authorRepository.deleteByIdNativeQuery(id) == DELETE_FAILED)
            throw new EntityNotFoundException("Author not found.");
    }

}
