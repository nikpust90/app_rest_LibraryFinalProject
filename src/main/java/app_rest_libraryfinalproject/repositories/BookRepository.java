package app_rest_libraryfinalproject.repositories;

import app_rest_libraryfinalproject.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
