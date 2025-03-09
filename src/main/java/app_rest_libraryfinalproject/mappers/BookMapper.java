package app_rest_libraryfinalproject.mappers;

import app_rest_libraryfinalproject.dto.BookDto;
import app_rest_libraryfinalproject.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {



    public List<BookDto> toListDto(List<Book> books) {
        return books.stream()
                .map(this::toDto)
                .toList();
    }

    public BookDto toDto(Book book) {
        return new BookDto(
                book.getName(),
                book.getYearOfProduction(),
                book.getAuthor(),
                book.getAnnotation()
        );

    }


}
