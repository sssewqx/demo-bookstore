package me.bookstore.demo.mapper;

import me.bookstore.demo.dto.BookDto;
import me.bookstore.demo.entity.Author;
import me.bookstore.demo.entity.Book;
import me.bookstore.demo.repository.AuthorRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authorId", source = "author.id")
    BookDto bookToBookDto(Book book);
    @Mapping(target = "authorId", source = "author.id")
    List<BookDto> bookToBookDto(List<Book> books);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", expression = "java(getAuthorById(bookDto.authorId(), authorRepository))")
    Book bookDtoToBook(BookDto bookDto, @Context AuthorRepository authorRepository, @MappingTarget Book book);

    default Author getAuthorById(UUID id, @Context AuthorRepository authorRepository) {
        return authorRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.")
        );
    }

}
