package app_rest_libraryfinalproject.controllers;

import app_rest_libraryfinalproject.dto.BookDto;
import app_rest_libraryfinalproject.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public ResponseEntity<List<BookDto>> GetAllBooks() {
        log.info("Запрос на получение всех книг");
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Получить книгу по ID.
     * <p>
     * Этот метод возвращает книгу по уникальному идентификатору.
     * Если книга не найдена, будет выброшено исключение.
     *
     * @param id ID книги.
     * @return ResponseEntity с книгой или сообщением об ошибке.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        log.info("Запрос на получение книги с ID {}", id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    /**
     * Получить список книг, которые находятся у конкретного пользователя.
     * <p>
     * Этот метод возвращает список книг, принадлежащих определенному пользователю.
     *
     * @param userId ID пользователя.
     * @return ResponseEntity со списком книг или сообщением об ошибке.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookDto>> getBooksByUser(@PathVariable Long userId) {
        log.info("Запрос на получение книг для пользователя с ID {}", userId);
        return ResponseEntity.ok(bookService.getBooksByUser(userId));
    }

    /**
     * Назначить книгу текущему пользователю.
     * <p>
     * Этот метод позволяет текущему пользователю забрать книгу себе.
     * Если книга уже назначена пользователю, будет выведено соответствующее сообщение.
     *
     * @param bookId ID книги.
     * @return ResponseEntity с сообщением о результате.
     */
    @PostMapping("/assign/{id}")
    public ResponseEntity<String> assignBookToCurrentUser(@PathVariable("id") Long bookId) {
        log.info("Запрос на передачу книги с ID {} текущему пользователю", bookId);
        bookService.assignBookToCurrentUser(bookId);  // Используем сервисный метод
        return ResponseEntity.ok("Книга успешно передана текущему пользователю");
    }

    /**
     * Создание новой книги.
     * <p>
     * Этот метод позволяет создать новую книгу.
     * Доступен только пользователю с ролью ADMIN.
     *
     * @param bookDto объект Book, который приходит в теле запроса.
     * @return ResponseEntity с созданной книгой или сообщением об ошибке.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid BookDto bookDto) {
        log.info("Запрос на создание новой книги: {}", bookDto);
        BookDto createdBook = bookService.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * Обновление существующей книги.
     * <p>
     * Этот метод позволяет обновить данные существующей книги.
     * Доступен только пользователю с ролью ADMIN.
     *
     * @param id      ID книги.
     * @param bookDto объект Book из тела запроса.
     * @return ResponseEntity с обновленной книгой.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        log.info("Запрос на обновление книги с ID {}", id);
        BookDto updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Удаление книги.
     * <p>
     * Этот метод позволяет удалить книгу по ее ID.
     * Доступен только пользователю с ролью ADMIN.
     *
     * @param id ID книги, которую необходимо удалить.
     * @return ResponseEntity без содержимого.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        log.info("Запрос на удаление книги с ID {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.ok(Map.of("status", "deleted"));
    }

    /**
     * Получение обложки книги.
     * <p>
     * Этот метод возвращает обложку книги в виде изображения. Если файл не найден, возвращается ошибка 404.
     *
     * @param bookId ID книги.
     * @return ResponseEntity с изображением или сообщением об ошибке.
     */
    @GetMapping("/{bookId}/cover")
    public ResponseEntity<FileSystemResource> getBookCover(@PathVariable Long bookId) {
        log.info("Запрос на получение обложки книги с ID {}", bookId);
        // Путь к файлу обложки книги (например, на сервере)
        String filePath = "C:" + File.separator + "Обложки" + File.separator + bookId + "_cover.jpg";
        Path imagePath = Paths.get(filePath);

        // Проверяем, существует ли файл
        FileSystemResource resource = new FileSystemResource(imagePath);
        if (!resource.exists()) {
            log.warn("Обложка книги с ID {} не найдена", bookId);
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

