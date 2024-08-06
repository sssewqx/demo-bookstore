package me.bookstore.demo.mapper;

import me.bookstore.demo.dto.AuthorDto;
import me.bookstore.demo.entity.Author;
import me.bookstore.demo.entity.Book;
import me.bookstore.demo.repository.BookRepository;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "booksId", expression = "java(booksToBooksId(author.getBooks()))")
    AuthorDto authorToAuthorDto(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", expression = "java(booksIdToBooks(authorDto.booksId(), bookRepository))")
    Author authorDtoToAuthor(AuthorDto authorDto, @Context BookRepository bookRepository, @MappingTarget Author author);

    @Mapping(target = "booksId", expression = "java(booksToBooksId(author.getBooks()))")
    List<AuthorDto> authorToAuthorDto(List<Author> authors);

    @Named("booksToBooksId")
    default List<UUID> booksToBooksId(List<Book> books) {
        if (books == null) {
            return null;
        }
        return books.stream()
                .map(Book::getId)
                .collect(Collectors.toList());
    }

    @Named("booksIdToBooks")
    default List<Book> booksIdToBooks(List<UUID> booksId, @Context BookRepository bookRepository) {
        if (booksId == null) {
            return null;
        }
        return booksId.stream()
                .map(bookRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}

