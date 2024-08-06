package me.bookstore.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BookDto (
        @NotBlank
        String title,
        @NotNull
        UUID authorId) {}

