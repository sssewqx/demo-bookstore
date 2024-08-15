package me.bookstore.demo.controller;

import jakarta.transaction.Transactional;
import me.bookstore.demo.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
public class AuthorControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final UUID AUTHOR_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Test
    @DisplayName("Получить всех авторов")
    public void testGetAuthors() throws Exception {
        mockMvc.perform(get("/authors/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Получить автора по ID")
    public void testGetAuthorById() throws Exception {
        mockMvc.perform(get("/authors/{id}", AUTHOR_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Создать автора")
    public void testCreateAuthor() throws Exception {
        mockMvc.perform(post("/authors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Новое Имя\", \"lastName\": \"Новая Фамилия\", \"booksId\": []}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Обновить автора")
    public void testUpdateAuthor() throws Exception {
        mockMvc.perform(patch("/authors/update")
                        .param("authorId", AUTHOR_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Обновленное Имя\", \"lastName\": \"Обновленная Фамилия\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Удалить автора")
    public void testDeleteAuthor() throws Exception {

        mockMvc.perform(delete("/authors/{id}", AUTHOR_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}