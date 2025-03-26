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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;
    private final PeopleRepository peopleRepository;

    // Получить список книг
    public List<BookDto> getAllBooks() {
        return repository.findAll()
                .stream()
                .map(mapper::toDtoForGet)
                .toList();
    }

    // Получить книгу по ID
    @Transactional
    public BookDto getBookById(Long id) {
        log.debug("Получаем книгу с ID: {}", id);
        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга с ID " + id + " не найдена"));
        return mapper.toDtoForGet(book);
    }

    // Получить книги, принадлежащие конкретному пользователю
    @Transactional
    public List<BookDto> getBooksByUser(Long userId) {
        log.debug("Получаем книги пользователя с ID: {}", userId);
        return repository.findByOwnerId(userId).stream()
                .map(mapper::toDtoForGet)
                .toList();
    }

    // Назначить книгу текущему пользователю
    @Transactional
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
    public BookDto createBook(BookDto bookDto, MultipartFile coverFile) {
        log.info("Создание книги: {}", bookDto.getName());

        Optional<Book> existingBook = repository.findByName(bookDto.getName());
        if (existingBook.isPresent()) {
            throw new RuntimeException("Книга с названием '" + bookDto.getName() + "' уже существует");
        }

        String currentUser = UserUtils.getCurrentUsername();
        Book book = mapper.toEntity(bookDto);
        book.setCreatedPerson(currentUser);
        book.setCreatedAt(UserUtils.getCurrentTime());

        // Сохранение обложки, если она есть
        if (coverFile != null && !coverFile.isEmpty()) {
            book = repository.save(book); // Сначала сохраняем книгу, чтобы получить ID
            try {
                String coverPath = saveCoverImage(book.getId(), coverFile);
                book.setCoverPath(coverPath);
            } catch (IOException e) {
                log.error("Ошибка при сохранении обложки книги с ID {}: {}", book.getId(), e.getMessage());
                throw new RuntimeException("Не удалось сохранить обложку книги", e);
            }
        }

        return mapper.toDtoForCreate(repository.save(book));
    }

    private String saveCoverImage(Long bookId, MultipartFile file) throws IOException {
        String uploadDir = "C:" + File.separator + "Обложки";
        Files.createDirectories(Paths.get(uploadDir)); // Создаём папку, если её нет

        String filename = bookId + "_cover.jpg"; // Файл сохраняется по ID книги
        Path filePath = Paths.get(uploadDir, filename);
        Files.write(filePath, file.getBytes()); // Здесь может быть UncheckedIOException

        return filePath.toString(); // Сохраняем путь в БД
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


