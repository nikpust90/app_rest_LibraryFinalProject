package app_rest_libraryfinalproject.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    String name;

    Integer yearOfProduction;

    String author;


    String annotation;
}
