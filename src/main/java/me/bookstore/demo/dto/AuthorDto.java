package me.bookstore.demo.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public record AuthorDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Nullable
        List<UUID> booksId
) { }
