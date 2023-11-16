package ru.katishev.Project2Boot.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.katishev.Project2Boot.models.Book;
import ru.katishev.Project2Boot.models.Person;
import ru.katishev.Project2Boot.repositories.BooksRepository;
import ru.katishev.Project2Boot.repositories.PeopleRepository;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setBookId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id) {
        Book book = booksRepository.findById(id).orElse(new Book());
        return Optional.ofNullable(book.getOwner());
    }

    @Transactional
    public void assign(int bookId, int id) {
        Book book = booksRepository.findById(bookId).orElse(new Book());
        Person newOwner = peopleRepository.findById(id).orElse(null);

        book.setDateOfAssign(new Date());
        book.setOwner(newOwner);
    }

    @Transactional
    public void release(int id) {
        Book book = booksRepository.findById(id).orElse(new Book());
        book.setDateOfAssign(null);
        book.setOwner(null);
    }

    public List<Book> getBooksPerPage(String page, String booksPerPage) {
        int pageNumber;
        int countOfBooks;

        try {
            pageNumber = Integer.parseInt(page);
            countOfBooks = Integer.parseInt(booksPerPage);
        } catch (NumberFormatException e) {
            return booksRepository.findAll();
        }

        return booksRepository.findAll(PageRequest.of(pageNumber, countOfBooks)).getContent();
    }

    public List<Book> getSortedBooks() {
        return booksRepository.findAll(Sort.by("year"));
    }

    public List<Book> getSortedByNameBooks() {
        return booksRepository.findAll(Sort.by("name"));
    }

    public List<Book> getSortedBooksPerPage(String page, String booksPerPage) {
        int pageNumber;
        int countOfBooks;

        try {
            pageNumber = Integer.parseInt(page);
            countOfBooks = Integer.parseInt(booksPerPage);
        } catch (NumberFormatException e) {
            return booksRepository.findAll();
        }

        return booksRepository.findAll(PageRequest.of(pageNumber, countOfBooks,Sort.by("year"))).getContent();
    }

    public List<Book> findForName(String query) {
        return booksRepository.findByNameStartsWith(query);
    }

    public List<Book> findFreeBooks() {
        List<Book> books = booksRepository.findByOwnerIsNull();
        books.sort(Comparator.comparing(Book::getName));
        return books;
    }
}