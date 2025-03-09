package app_rest_libraryfinalproject.service;

import app_rest_libraryfinalproject.dto.BookDto;
import app_rest_libraryfinalproject.dto.PersonDTO;
import app_rest_libraryfinalproject.mappers.BookMapper;
import app_rest_libraryfinalproject.model.Book;
import app_rest_libraryfinalproject.model.Person;
import app_rest_libraryfinalproject.repositories.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    public List<BookDto> getAllBooks() {
        List<Book> books = repository.findAll();
        return mapper.toListDto(books);
    }

    @Transactional
    public BookDto createBook(BookDto bookDto) {
        log.info("Создание книги: {}", bookDto.getName());

        // Проверяем, есть ли уже книга с таким названием
        Optional<Book> existingBook = repository.findByName(bookDto.getName());
        if (existingBook.isPresent()) {
            throw new RuntimeException("Книга с названием '" + bookDto.getName() + "' уже существует");
        }

        Book book = mapper.toEntity(bookDto);
        return mapper.toDtoForCreate(repository.save(book));


    }


    @Transactional
    public BookDto updateBook(BookDto bookDto) {
        log.info("Обновление книги: {}", bookDto.getName());


    }
}
