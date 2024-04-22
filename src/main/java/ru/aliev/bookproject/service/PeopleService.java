package ru.aliev.bookproject.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aliev.bookproject.models.Book;
import ru.aliev.bookproject.models.Person;
import ru.aliev.bookproject.repository.BooksRepository;
import ru.aliev.bookproject.repository.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PeopleService {

    private PeopleRepository peopleRepository;
    private BooksRepository booksRepository;

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooksByOwnerId(int id) {
        List<Book> books = booksRepository.getBooksByOwnerId(id);

        for (Book book : books) {
            if (book.getOwner() != null) {
                long diff = new Date().getTime() - book.getTookDate().getTime();
                System.out.println("tookDate: " + book.getTookDate().getTime());
                System.out.println("curDate: " + new Date().getTime());
                if (diff > 10 * 24 * 60 * 60 * 1000) {
                    book.setExpired(true);
                    System.out.println(book);
                }
            }
        }

        return books;
    }
}
