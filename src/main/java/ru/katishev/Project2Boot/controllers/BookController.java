package ru.katishev.Project2Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.katishev.Project2Boot.models.Book;
import ru.katishev.Project2Boot.models.Person;
import ru.katishev.Project2Boot.services.BooksService;
import ru.katishev.Project2Boot.services.PeopleService;
import ru.katishev.Project2Boot.util.BookValidator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;
    private final PeopleService peopleService;
    private final BookValidator bookValidator;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) String page,
                        @RequestParam(value = "books_per_page", required = false) String booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) String sort) {

        boolean isSort = sort != null && sort.equals("true");

        List<Book> books;
        if (page != null && booksPerPage != null && isSort) {
            books = booksService.getSortedBooksPerPage(page, booksPerPage);
        } else if (page != null && booksPerPage != null) {
            books = booksService.getBooksPerPage(page, booksPerPage);
        } else if (isSort) {
            books = booksService.getSortedBooks();
        } else {
            books = booksService.getSortedByNameBooks();
        }
        model.addAttribute("books", books);

        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping("")
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Optional<Person> bookOwner = booksService.getBookOwner(id);

        if (bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        } else {
            model.addAttribute("people", peopleService.findAll());
        }

        model.addAttribute("person", new Person());
        model.addAttribute("book", booksService.findOne(id));

        return "books/show";
    }

    @PatchMapping("{id}/assign")
    public String assign(@PathVariable("id") int bookId,
                         @ModelAttribute("person") Person person) {
        booksService.assign(bookId, person.getId());
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("{id}/newassign")
    public String newAssign(@PathVariable("id") int personId,
                         @ModelAttribute("book")Book b) {
        Book book = booksService.findOne(b.getBookId());
        if (book.getOwner() != null) {
            return "redirect:/people/" + personId;
        }

        booksService.assign(b.getBookId(), personId);

        return "redirect:/people/" + personId;
    }

    @PatchMapping("{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", booksService.findForName(query));
        return "books/search";
    }
}
