package app_rest_libraryfinalproject.dto;

import app_rest_libraryfinalproject.model.Person;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto {

    private Long id;  // Добавляем ID

    @NotBlank(message = "Название книги не может быть пустым")
    @Size(max = 255, message = "Название книги не должно превышать 255 символов")
    private String name;

    @NotNull(message = "Год выпуска не может быть пустым")
    private Integer yearOfProduction;

    @NotBlank(message = "Автор книги не может быть пустым")
    private String author;

    @Size(max = 1000, message = "Аннотация не должна превышать 1000 символов")
    private String annotation;

    Boolean isBookDeleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime removedAt;

    private String createdPerson;
    private String updatedPerson;
    private String removedPerson;

    private Long ownerId;

    public BookDto(String name, Integer yearOfProduction, String author, String annotation,
                   String person, LocalDateTime timestamp, boolean isUpdate) {
        this.name = name;
        this.yearOfProduction = yearOfProduction;
        this.author = author;
        this.annotation = annotation;
        if (isUpdate) {
            this.updatedPerson = person;
            this.updatedAt = timestamp;
        } else {
            this.createdPerson = person;
            this.createdAt = timestamp;
        }
    }

    public BookDto(String name, Integer yearOfProduction, String author, String annotation) {
        this.name = name;
        this.yearOfProduction = yearOfProduction;
        this.author = author;
        this.annotation = annotation;
    }




}
