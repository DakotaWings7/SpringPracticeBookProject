package ru.aliev.bookproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.aliev.bookproject.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    @Query(value = "SELECT Person.* FROM Book JOIN Person ON Book.owner_id = Person.id WHERE Book.id = ?", nativeQuery = true)
    public Optional<Person> findOwnerByBookId(int id);

}
