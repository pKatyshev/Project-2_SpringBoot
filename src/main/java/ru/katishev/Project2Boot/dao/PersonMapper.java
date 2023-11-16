package ru.katishev.Project2Boot.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.katishev.Project2Boot.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
