package me.bookstore.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bookstore.demo.dto.AuthorDto;
import me.bookstore.demo.dto.AuthorUpdateRequest;
import me.bookstore.demo.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testGetAuthors() throws Exception {
        given(authorService.getAllAuthors()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/authors/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetAuthorById() throws Exception {
        UUID authorId = UUID.randomUUID();
        AuthorDto authorDto = new AuthorDto("John", "Doe", null);
        given(authorService.getAuthorById(authorId)).willReturn(authorDto);

        mockMvc.perform(get("/authors/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorDto authorDto = new AuthorDto("John", "Doe", null);
        UUID authorId = UUID.randomUUID();
        given(authorService.createAuthor(any(AuthorDto.class))).willReturn(authorId);

        mockMvc.perform(post("/authors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("\"" + authorId.toString() + "\""));
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        UUID authorId = UUID.randomUUID();
        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest("John", "Doe");
        given(authorService.updateAuthor(any(UUID.class), any(AuthorUpdateRequest.class))).willReturn(updateRequest);

        mockMvc.perform(patch("/authors/update")
                        .param("authorId", authorId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        UUID authorId = UUID.randomUUID();

        mockMvc.perform(delete("/authors/{id}", authorId))
                .andExpect(status().isNoContent());
    }
}