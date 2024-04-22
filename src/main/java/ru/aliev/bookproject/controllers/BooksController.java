package ru.aliev.bookproject.controllers;


import jakarta.servlet.ServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aliev.bookproject.models.Book;
import ru.aliev.bookproject.models.Person;
import ru.aliev.bookproject.service.BooksService;
import ru.aliev.bookproject.service.PeopleService;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @GetMapping()
    public String index(Model model,
                        @RequestParam("sort_by_year") Optional<Boolean> sortByYear,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("books_per_page") Optional<Integer> booksPerPage) {
        List<Book> books = booksService.findAll();

        if (page.isPresent() && booksPerPage.isPresent()) {
            if (sortByYear.isPresent())
                books = booksService.findAll(page.get(), booksPerPage.get(), "publicationYear");
            else
                books = booksService.findAll(page.get(), booksPerPage.get());
        } else if (sortByYear.isPresent()) {
            books = booksService.findAll("publicationYear");
        }

        model.addAttribute("books", books);

        return "books/index";
    }

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(value = "title", required = false) String title,
                         @ModelAttribute("title") String regex) {
        model.addAttribute("books", booksService.findBooksByTitleStartingWith(title));

        return "books/search";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findById(id));

        Optional<Person> bookOwner = Optional.ofNullable(booksService.getBookOwner(id));

        if (bookOwner.isPresent())
            model.addAttribute("bookOwner", bookOwner);
        else
            model.addAttribute("people", peopleService.findAll());


        return "books/show";
    }

    @GetMapping("/new")
    public String createPage(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") Book book) {
        booksService.save(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id,
                           Model model) {
        model.addAttribute("book", booksService.findById(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") Book book) {
        booksService.update(id, book);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);

        return "redirect:/books";
    }

    @PatchMapping("/{id}/reserve")
    public String reserve(@PathVariable("id") int id,
                          @ModelAttribute("person") Person person) {
        booksService.reserve(id, person);

        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id,
                          @ModelAttribute("book") Book book) {
        booksService.release(id);

        return "redirect:/books/" + id;
    }
}
