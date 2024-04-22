package ru.aliev.bookproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aliev.bookproject.models.Book;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    public List<Book> getBooksByOwnerId(int id);

    public List<Book> findBooksByTitleStartingWith(String regex);
}
