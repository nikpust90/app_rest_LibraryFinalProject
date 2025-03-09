package app_rest_libraryfinalproject.service;

import app_rest_libraryfinalproject.dto.BookDto;
import app_rest_libraryfinalproject.dto.PersonDTO;
import app_rest_libraryfinalproject.mappers.BookMapper;
import app_rest_libraryfinalproject.model.Book;
import app_rest_libraryfinalproject.model.Person;
import app_rest_libraryfinalproject.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    public List<BookDto> getAllBooks() {
        List<Book> books = repository.findAll();
        return mapper.toListDto(books);
    }
}
