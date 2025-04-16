package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.Book;
import com.bookshop.bookstore.model.CartItem;
import com.bookshop.bookstore.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final BookRepository bookRepository;

    public CartController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, HttpSession session) {
    Book book = bookRepository.findById(bookId).orElseThrow();
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    if (cart == null) cart = new ArrayList<>();

    Optional<CartItem> existing = cart.stream()
            .filter(item -> item.getBook().getId().equals(bookId))
            .findFirst();

    if (existing.isPresent()) {
        existing.get().setQuantity(existing.get().getQuantity() + 1);
    } else {
        cart.add(new CartItem(book, 1));
    }

    session.setAttribute("cart", cart);
    session.setAttribute("addedToCart", true); 
    return "redirect:/booklist";
}


    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable Long bookId, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getBook().getId().equals(bookId));
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }
}
