package app_rest_libraryfinalproject.controllers;

import app_rest_libraryfinalproject.dto.BookDto;
import app_rest_libraryfinalproject.model.Book;
import app_rest_libraryfinalproject.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * Получение списка всех книг.
     *
     * @return ResponseEntity со списком книг или сообщением об ошибке.
     */
    @GetMapping()
    public ResponseEntity<List<BookDto>> GetAllBooks() {

        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}/cover")
    public ResponseEntity<FileSystemResource> getBookCover(@PathVariable Long bookId) {
        // Путь к файлу обложки книги (например, на сервере)
        Path imagePath = Paths.get("/path/to/images/" + bookId + "_cover.jpg");

        // Проверяем, существует ли файл
        FileSystemResource resource = new FileSystemResource(imagePath);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();  // Если файла нет, вернуть 404
        }

        // Устанавливаем заголовки для правильного отображения изображения
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + bookId + "_cover.jpg");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
