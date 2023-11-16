package ru.katishev.Project2Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.katishev.Project2Boot.models.Book;
import ru.katishev.Project2Boot.models.Person;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findByAuthor(String author);

    List<Book> findByOwner(Person person);

    List<Book> findByNameStartsWith(String name);

    List<Book> findByOwnerIsNull();
}
