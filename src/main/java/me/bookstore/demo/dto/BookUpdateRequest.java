package me.bookstore.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record BookUpdateRequest(
        @NotBlank
        String title
) { }
