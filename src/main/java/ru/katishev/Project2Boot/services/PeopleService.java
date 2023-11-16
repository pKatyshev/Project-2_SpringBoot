package ru.katishev.Project2Boot.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.katishev.Project2Boot.models.Book;
import ru.katishev.Project2Boot.models.Person;
import ru.katishev.Project2Boot.repositories.BooksRepository;
import ru.katishev.Project2Boot.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private PeopleRepository peopleRepository;
    private BooksRepository booksRepository;

    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBookForPerson(Person person) {
        List<Book> books = booksRepository.findByOwner(person);

        if (books != null) {
            for (Book book : books) {
                if (book.getDateOfAssign() == null) continue;

                long diff =  new Date().getTime() - book.getDateOfAssign().getTime();

                if (diff > 864000000) {
                    book.setOverdue(true);
                }
            }
        }

        return books;

    }
}
