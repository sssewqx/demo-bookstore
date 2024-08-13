package me.bookstore.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import me.bookstore.demo.dto.BookDto;
import me.bookstore.demo.dto.BookUpdateRequest;
import me.bookstore.demo.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Получить все книги")
    public void testGetAllBooks() throws Exception {
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();
        BookDto book1 = new BookDto("Book One", authorId1);
        BookDto book2 = new BookDto("Book Two", authorId2);
        List<BookDto> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Book One")))
                .andExpect(jsonPath("$[0].authorId", is(authorId1.toString())))
                .andExpect(jsonPath("$[1].title", is("Book Two")))
                .andExpect(jsonPath("$[1].authorId", is(authorId2.toString())));
    }

    @Test
    @DisplayName("Получить книгу по ID")
    public void testGetBookById() throws Exception {
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        BookDto book = new BookDto("Book One", authorId);

        when(bookService.getBookById(bookId)).thenReturn(book);

        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Book One")))
                .andExpect(jsonPath("$.authorId", is(authorId.toString())));
    }

    @Test
    @DisplayName("Создать книгу")
    public void testCreateBook() throws Exception {
        UUID authorId = UUID.randomUUID();
        BookDto book = new BookDto("New Book", authorId);
        UUID bookId = UUID.randomUUID();

        when(bookService.createBook(book)).thenReturn(bookId);

        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\",\"authorId\":\"" + authorId.toString() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString().replace("\"", "");
                    UUID returnedUuid = UUID.fromString(responseContent);
                    assertEquals(bookId, returnedUuid);
                });
    }

    @Test
    @DisplayName("Обновить книгу")
    public void testUpdateBook() throws Exception {
        UUID bookId = UUID.randomUUID();
        BookUpdateRequest updateRequest = new BookUpdateRequest("Updated Book");

        when(bookService.updateBook(bookId, updateRequest)).thenReturn(updateRequest);

        mockMvc.perform(patch("/books/update")
                        .param("id", bookId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Book\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Book")));
    }

    @Test
    @DisplayName("Удалить книгу")
    public void testDeleteBook() throws Exception {
        UUID bookId = UUID.randomUUID();

        doNothing().when(bookService).deleteBook(bookId);

        mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isNoContent());
    }
}