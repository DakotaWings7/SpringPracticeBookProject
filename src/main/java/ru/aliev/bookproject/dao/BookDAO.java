package ru.aliev.bookproject.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aliev.bookproject.models.Book;
import ru.aliev.bookproject.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update(
                "INSERT INTO Book(title, author, publicationyear) VALUES(?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getPublicationYear());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update(
                "UPDATE Book SET title=?, author=?, publicationyear=?, ownerisd=? WHERE id=?",
                book.getTitle(), book.getAuthor(), book.getPublicationYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }

    public Optional<Person> getBookOwner(int id) {
        return jdbcTemplate.query(
                "SELECT Person.* FROM Book JOIN Person ON Book.ownerid = Person.id WHERE Book.id = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void reserve(int id, Person person) {
        jdbcTemplate.update("UPDATE Book SET ownerid=? WHERE id=?", person.getId(), id);
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE Book SET ownerId=? WHERE id=?", null, id);
    }
}
