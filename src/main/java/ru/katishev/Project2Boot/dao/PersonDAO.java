package ru.katishev.Project2Boot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.katishev.Project2Boot.models.Book;
import ru.katishev.Project2Boot.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        List<Person> list = jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class));

        if (list.size() > 0) {
            return list.get(0);
        } else return null;
    }

    public Optional<Person> showOnName(String fullName) {
        return jdbcTemplate.query("SELECT * FROM person WHERE full_name=?", new Object[] {fullName},
                new BeanPropertyRowMapper<>(Person.class))
                .stream()
                .findAny();
    }

    public Optional<Person> showOnYear(int year) {
        return jdbcTemplate.query("SELECT * FROM person WHERE year_of_birth=?", new Object[]{year}, new BeanPropertyRowMapper<>(Person.class))
                .stream()
                .findAny();
    }

    public void save(Person person) {
        jdbcTemplate.update("insert into person(full_name, year_of_birth) values(?, ?)", person.getFullName(), person.getYearOfBirth());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET full_name=?, year_of_birth=? WHERE id=?",
                updatedPerson.getFullName(), updatedPerson.getYearOfBirth(), id);

    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    public List<Book> getBookForPerson(int personId) {
        List<Book> books;
        books = jdbcTemplate.query("SELECT * FROM book WHERE person_id=?", new Object[]{personId}, new BookMapper());
        return books;
    }

}
