package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.Book;
import com.bookshop.bookstore.model.CartItem;
import com.bookshop.bookstore.model.BookOrder;
import com.bookshop.bookstore.model.OrderItem;
import com.bookshop.bookstore.repository.BookRepository;
import com.bookshop.bookstore.repository.OrderRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    public CartController(BookRepository bookRepository, OrderRepository orderRepository ) {
    this.bookRepository = bookRepository;
    this.orderRepository = orderRepository;
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

    @PostMapping("/checkout")
    public String checkout(HttpSession session, Principal principal, Model model) {
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    if (cart == null || cart.isEmpty()) {
        model.addAttribute("message", "Cart is empty!");
        return "cart";
    }

    BookOrder order = BookOrder.builder()
            .username(principal.getName())
            .date(LocalDateTime.now())
            .build();

    List<OrderItem> orderItems = new ArrayList<>();

    for (CartItem item : cart) {
        Book book = item.getBook();
        if (book.getStock() < item.getQuantity()) {
            model.addAttribute("message", "Not enough stock for " + book.getTitle());
            return "cart";
        }
        book.setStock(book.getStock() - item.getQuantity());
        bookRepository.save(book); 

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .book(book)
                .quantity(item.getQuantity())
                .priceAtPurchase(book.getPrice())
                .build();

        orderItems.add(orderItem);
    }

    order.setItems(orderItems);

    orderRepository.save(order);

    session.removeAttribute("cart");

    model.addAttribute("order", order);
    return "checkout-success";
}

}
