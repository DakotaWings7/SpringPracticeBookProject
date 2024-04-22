package ru.aliev.bookproject.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aliev.bookproject.models.Book;
import ru.aliev.bookproject.models.Person;
import ru.aliev.bookproject.repository.BooksRepository;
import ru.aliev.bookproject.repository.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BooksService {

    private BooksRepository booksRepository;
    private PeopleRepository peopleRepository;

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(String field) {
        return booksRepository.findAll(Sort.by(field));
    }

    public List<Book> findAll(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> findAll(int page, int booksPerPage, String field) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by(field))).getContent();
    }

    public Book findById(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    public List<Book> findBooksByTitleStartingWith(String regex) {
        return booksRepository.findBooksByTitleStartingWith(regex);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Person getBookOwner(int id) {
        return peopleRepository.findOwnerByBookId(id).orElse(null);
    }

    @Transactional
    public void reserve(int id, Person person) {
        Optional<Book> bookToUpdate = booksRepository.findById(id);
        if (bookToUpdate.isPresent()) {
            bookToUpdate.get().setOwner(person);
            bookToUpdate.get().setTookDate(new Date());
        }
    }

    @Transactional
    public void release(int id) {
        Optional<Book> bookToUpdate = booksRepository.findById(id);
        if (bookToUpdate.isPresent()) {
            bookToUpdate.get().setOwner(null);
            bookToUpdate.get().setTookDate(null);
        }
    }
}
