package me.bookstore.demo.mapper;

import java.util.ArrayList;
import java.util.Collections;
import me.bookstore.demo.dto.AuthorDto;
import me.bookstore.demo.entity.Author;
import me.bookstore.demo.entity.Book;
import me.bookstore.demo.repository.BookRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

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
        if (booksId == null || booksId.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(bookRepository.findAllById(booksId));
    }
}

