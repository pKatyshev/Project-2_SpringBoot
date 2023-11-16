package ru.katishev.Project2Boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.katishev.Project2Boot.dao.PersonDAO;
import ru.katishev.Project2Boot.models.Person;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if (personDAO.showOnName(person.getFullName()).isPresent()) {
            if (personDAO.showOnYear(person.getYearOfBirth()).isPresent()) {
                errors.rejectValue("fullName", "", "Полный тёзка");
            }
        }
    }
}
