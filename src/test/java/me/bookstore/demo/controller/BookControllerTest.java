package me.bookstore.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.transaction.Transactional;
import me.bookstore.demo.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@Transactional
public class BookControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final UUID BOOK_ID = UUID.fromString("2e79d637-a309-43c0-a59e-7018985f612f");
    private final UUID AUTHOR_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Test
    @DisplayName("Получить все книги")
    public void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/books/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Получить книгу по ID")
    public void testGetBookById() throws Exception {
        mockMvc.perform(get("/books/{id}", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Создать книгу")
    public void testCreateBook() throws Exception {
        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Новая книга\", \"authorId\": \"" + AUTHOR_ID + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Обновить книгу")
    public void testUpdateBook() throws Exception {
        mockMvc.perform(patch("/books/update")
                        .param("id", BOOK_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Обновленная книга\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Удалить книгу")
    public void testDeleteBook() throws Exception {

        mockMvc.perform(delete("/books/{id}", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
