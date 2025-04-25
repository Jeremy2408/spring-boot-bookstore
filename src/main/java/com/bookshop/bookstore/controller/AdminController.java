package com.bookshop.bookstore.controller;
import com.bookshop.bookstore.model.Book;
import com.bookshop.bookstore.repository.BookRepository;
import com.bookshop.bookstore.repository.ReviewRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookRepository repo;
    private final ReviewRepository reviewRepo;


    public AdminController(BookRepository repo, ReviewRepository reviewRepo) {
        this.repo = repo;
        this.reviewRepo = reviewRepo;
    }

    

    @GetMapping("/edit-stock")
    public String showStockEditor(@RequestParam(required = false) Boolean updated, Model model) {
        model.addAttribute("books", repo.findAll());
        if (updated != null && updated) {
            model.addAttribute("success", true);
        }
        return "edit-stock";
    }

    @PostMapping("/update-stock")
    public String updateStock(@RequestParam Map<String, String> params) {
        for (String key : params.keySet()) {
            if (key.startsWith("stock_")) {
                Long bookId = Long.parseLong(key.replace("stock_", ""));
                int newStock = Integer.parseInt(params.get(key));
                Book book = repo.findById(bookId).orElseThrow();
                book.setStock(newStock);
                repo.save(book);
            }
        }
        return "redirect:/admin/edit-stock?updated=true";
    }

    @PostMapping("/delete-book/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBook(@PathVariable Long id) {
        Book book = repo.findById(id).orElseThrow(); 
        book.setActive(false); 
        repo.save(book); 
        return "redirect:/booklist";
    }

    @PostMapping("/delete-review/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteReview(@PathVariable Long id, @RequestParam Long bookId) {
        reviewRepo.deleteById(id);
        return "redirect:/book/" + bookId;
    }


}
