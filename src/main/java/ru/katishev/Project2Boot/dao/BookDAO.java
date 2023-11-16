package ru.katishev.Project2Boot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.katishev.Project2Boot.models.Book;
import ru.katishev.Project2Boot.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book", new BookMapper());
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES (?,?,?)",
                book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book updatedBook) {
        if (updatedBook.getPersonId() == 0) {
            jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE book_id=?",
                    updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
        } else {
            jdbcTemplate.update("UPDATE book SET person_id=?, name=?, author=?, year=? WHERE book_id=?",
                    updatedBook.getPersonId(), updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
        }

    }

    public void delete(int book_id) {
        jdbcTemplate.update("DELETE FROM book WHERE book_id=?", book_id);
    }

    public Book show(int book_id) {
        List<Book> books = new ArrayList<>();
        books = jdbcTemplate.query("SELECT * FROM book WHERE book_id=?", new Object[]{book_id}, new BookMapper());

        if (books.size() > 0) {
            return books.get(0);
        } else return null;
    }

    public Optional<Book> show(String title) {
        return jdbcTemplate.query("SELECT * FROM book WHERE name=?", new Object[]{title}, new BookMapper())
                .stream()
                .findAny();
    }

    public void assign(int bookId, int personId) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE book_id=?", personId, bookId);
    }

    public void release(int bookId) {
        jdbcTemplate.update("UPDATE book SET person_id=null WHERE book_id=?", bookId);
    }

    public Optional<Person> getBookOwner(int bookId) {
        return jdbcTemplate.query("SELECT Person.* FROM book JOIN person ON book.person_id = person.id WHERE book.book_id=?",
                new Object[]{bookId}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
}
