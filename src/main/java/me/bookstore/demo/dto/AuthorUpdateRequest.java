package me.bookstore.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorUpdateRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName
) { }
