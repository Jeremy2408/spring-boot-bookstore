package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.Book;
import com.bookshop.bookstore.repository.BookRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BookViewController {

    private final BookRepository bookRepository;

    public BookViewController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/add-book")
        public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
        }

    @PostMapping("/add-book")
        public String addBook(@ModelAttribute Book book) {
        bookRepository.save(book);
    return "redirect:/booklist";
    }


    @GetMapping("/booklist")
    public String getBooks(
            @RequestParam(required = false) String searchField,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false, defaultValue = "title") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            HttpSession session,
            Model model) {

        List<Book> books;

        if (searchField != null && searchTerm != null && !searchTerm.isEmpty()) {
            switch (searchField) {
                case "author":
                    books = bookRepository.findByAuthorContainingIgnoreCase(searchTerm);
                    break;
                case "publisher":
                    books = bookRepository.findByPublisherContainingIgnoreCase(searchTerm);
                    break;
                case "category":
                    books = bookRepository.findByCategoryContainingIgnoreCase(searchTerm);
                    break;
                default:
                    books = bookRepository.findByTitleContainingIgnoreCase(searchTerm);
            }
        } else {
            books = bookRepository.findByActiveTrue(Sort.by(
                sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                    sortField
            ));
        }

        model.addAttribute("books", books);
        model.addAttribute("searchField", searchField);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        Boolean addedToCart = (Boolean) session.getAttribute("addedToCart");
        if (addedToCart != null && addedToCart) {
            model.addAttribute("addedToCart", true);
            session.removeAttribute("addedToCart");
}

        return "books";
    }
}
