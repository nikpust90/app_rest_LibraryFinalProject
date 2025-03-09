package app_rest_libraryfinalproject.controllers;

import app_rest_libraryfinalproject.dto.BookDto;
import app_rest_libraryfinalproject.model.Book;
import app_rest_libraryfinalproject.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import org.springframework.core.io.FileSystemResource;
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

    /**
     * Создание новой книги.
     *
     * @param book - объект Book, который приходит в теле запроса.
     * @return ResponseEntity с созданной книгой или сообщением об ошибке.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> createBook(
            @Valid @RequestBody BookDto book) {
        BookDto createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * Обновление существующей книги.
     *
     * @param book объект Book из тела запроса.
     * @return обновленная книга.
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> updateBook(
            @Valid @RequestBody BookDto book) {
        BookDto updatedBook = bookService.updateBook(book);
        return ResponseEntity.ok(updatedBook);
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
