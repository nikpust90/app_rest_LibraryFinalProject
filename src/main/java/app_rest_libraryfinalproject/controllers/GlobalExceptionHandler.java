package app_rest_libraryfinalproject.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработчик исключений для всех методов контроллера.
     * Это будет ловить все непредвиденные ошибки и возвращать соответствующий ответ.
     *
     * @param e Исключение, которое было выброшено.
     * @return Ответ с ошибкой.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Произошла ошибка: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ошибка: " + e.getMessage());
    }

    /**
     * Обработчик ошибки, если книга не найдена.
     *
     * @param e Исключение, которое выбрасывается, если книга не найдена.
     * @return Ответ с ошибкой.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleBookNotFound(NoSuchElementException e) {
        log.warn("Не найден объект: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Объект не найден: " + e.getMessage());
    }

    /**
     * Обработчик ошибки, если книга уже назначена пользователю.
     *
     * @param e Исключение, которое выбрасывается, если книга уже назначена.
     * @return Ответ с ошибкой.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleBookAlreadyAssigned(IllegalStateException e) {
        log.warn("Ошибка назначения книги: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
    }
}
