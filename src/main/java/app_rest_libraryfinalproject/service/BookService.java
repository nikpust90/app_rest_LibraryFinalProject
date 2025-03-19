package app_rest_libraryfinalproject.service;

import app_rest_libraryfinalproject.dto.BookDto;
import app_rest_libraryfinalproject.mappers.BookMapper;
import app_rest_libraryfinalproject.model.Book;
import app_rest_libraryfinalproject.model.Person;
import app_rest_libraryfinalproject.repositories.BookRepository;
import app_rest_libraryfinalproject.repositories.PeopleRepository;
import app_rest_libraryfinalproject.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;
    private final PeopleRepository peopleRepository;

    // Получить книгу по ID
    public BookDto getBookById(Long id) {
        log.debug("Получаем книгу с ID: {}", id);
        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга с ID " + id + " не найдена"));
        return mapper.toDtoForGet(book);
    }

    // Получить книги, принадлежащие конкретному пользователю
    public List<BookDto> getBooksByUser(Long userId) {
        log.debug("Получаем книги пользователя с ID: {}", userId);
        return repository.findByOwnerId(userId).stream()
                .map(mapper::toDtoForGet)
                .toList();
    }

    // Назначить книгу текущему пользователю
    @Transactional  // Операция, которая должна быть атомарной
    public void assignBookToCurrentUser(Long bookId) {
        String username = UserUtils.getCurrentUsername();
        log.info("Назначаем книгу с ID {} пользователю с username {}", bookId, username);

        Book book = repository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга с ID " + bookId + " не найдена"));

        Person person = peopleRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь с username " + username + " не найден"));

        if (book.getOwner() != null) {
            throw new RuntimeException("Книга уже принадлежит пользователю " + book.getOwner().getUsername());
        }

        book.setOwner(person);  // Назначаем книгу пользователю
        repository.save(book);  // Сохраняем изменения
    }

    // Создать новую книгу
    @Transactional
    public BookDto createBook(BookDto bookDto) {
        log.info("Создание книги: {}", bookDto.getName());
        // Проверяем, есть ли уже книга с таким названием
        Optional<Book> existingBook = repository.findByName(bookDto.getName());
        if (existingBook.isPresent()) {
            throw new RuntimeException("Книга с названием '" + bookDto.getName() + "' уже существует");
        }

        // Получаем имя текущего пользователя
        String currentUser = UserUtils.getCurrentUsername();

        Book book = mapper.toEntity(bookDto);  // Преобразуем DTO в сущность
        book.setCreatedPerson(currentUser);
        book.setCreatedAt(UserUtils.getCurrentTime());

        // Сохраняем книгу и возвращаем её DTO
        return mapper.toDtoForCreate(repository.save(book));
    }

    // Обновить информацию о книге
    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        log.info("Обновление книги с ID {}: {}", id, bookDto.getName());
        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга с ID " + id + " не найдена"));

        book.setName(bookDto.getName());
        book.setAuthor(bookDto.getAuthor());
        book.setAnnotation(bookDto.getAnnotation());
        book.setYearOfProduction(bookDto.getYearOfProduction());
        book.setUpdatedAt(UserUtils.getCurrentTime());
        book.setUpdatedPerson(UserUtils.getCurrentUsername());

        // Сохраняем обновления
        return mapper.toDtoForUpdate(repository.save(book));
    }

    // Удалить книгу
    @Transactional
    public void deleteBook(Long id) {
        log.info("Удаление книги с ID {}", id);
        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга с ID " + id + " не найдена"));

        book.setRemovedAt(UserUtils.getCurrentTime());
        book.setRemovedPerson(UserUtils.getCurrentUsername());
        book.setIsBookDeleted(true);

        // Сохраняем изменения
        repository.save(book);
    }
}


