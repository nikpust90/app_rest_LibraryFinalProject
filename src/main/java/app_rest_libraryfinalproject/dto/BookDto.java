package app_rest_libraryfinalproject.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    @NotBlank(message = "Название книги не может быть пустым")
    @Size(max = 255, message = "Название книги не должно превышать 255 символов")
    private String name;

    @NotNull(message = "Год выпуска не может быть пустым")
    private Integer yearOfProduction;

    @NotBlank(message = "Автор книги не может быть пустым")
    private String author;

    @Size(max = 1000, message = "Аннотация не должна превышать 1000 символов")
    private String annotation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime removedAt;

    private String createdPerson;
    private String updatedPerson;
    private String removedPerson;

    public BookDto(String name, Integer yearOfProduction, String author, String annotation, String createdPerson, LocalDateTime createdAt) {
    }

    public BookDto(String name, Integer yearOfProduction, String author, String annotation) {
    }
}
