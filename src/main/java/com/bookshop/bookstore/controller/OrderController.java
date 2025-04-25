
package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.BookOrder;
import com.bookshop.bookstore.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    private final OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping("/my-orders")
    public String viewMyOrders(Principal principal, Model model) {
        List<BookOrder> orders = orderRepo.findByUsername(principal.getName());
        model.addAttribute("orders", orders);
        return "my-orders";
    }
}

