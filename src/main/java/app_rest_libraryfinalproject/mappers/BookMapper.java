package app_rest_libraryfinalproject.mappers;

import app_rest_libraryfinalproject.dto.BookDto;
import app_rest_libraryfinalproject.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {



    // Маппинг для получения списка книг (без createdPerson и createdAt)
    public BookDto toDtoForGet(Book book) {
        return new BookDto(
                book.getName(),
                book.getYearOfProduction(),
                book.getAuthor(),
                book.getAnnotation()
        );
    }

    // Маппинг для ответа при создании книги (с createdPerson и createdAt)
    public BookDto toDtoForCreate(Book book) {
        return new BookDto(
                book.getName(),
                book.getYearOfProduction(),
                book.getAuthor(),
                book.getAnnotation(),
                book.getCreatedPerson(),
                book.getCreatedAt()
        );
    }

    public List<BookDto> toListDto(List<Book> books) {
        return books.stream()
                .map(this::toDtoForGet)
                .collect(Collectors.toList());
    }

    // Маппинг из DTO в Entity при создании книги
    public Book toEntity(BookDto bookDto) {
        return Book.builder()
                .name(bookDto.getName())
                .yearOfProduction(bookDto.getYearOfProduction())
                .author(bookDto.getAuthor())
                .annotation(bookDto.getAnnotation())
                .createdPerson(bookDto.getCreatedPerson())
                .createdAt(bookDto.getCreatedAt())
                .build();
    }


}
