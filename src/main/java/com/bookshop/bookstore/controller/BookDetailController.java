
package com.bookshop.bookstore.controller;
import com.bookshop.bookstore.model.Book;
import com.bookshop.bookstore.model.Review;
import com.bookshop.bookstore.repository.BookRepository;
import com.bookshop.bookstore.repository.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookDetailController {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    public BookDetailController(BookRepository bookRepository, ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow();
        List<Review> reviews = reviewRepository.findByBook(book);

        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewForm", new Review()); 

        return "book-details";
    }

    @PostMapping("/{id}/review")
    public String submitReview(
            @PathVariable Long id,
            @ModelAttribute("reviewForm") Review reviewForm,
            Principal principal) {

        Book book = bookRepository.findById(id).orElseThrow();

        reviewForm.setId(null);
        reviewForm.setBook(book);
        reviewForm.setUsername(principal.getName());

        reviewRepository.save(reviewForm);

        return "redirect:/book/" + id;
    }
}
