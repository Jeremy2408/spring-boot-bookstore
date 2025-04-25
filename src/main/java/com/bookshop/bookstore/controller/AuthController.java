package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.User;
import com.bookshop.bookstore.repository.UserRepository;

import java.security.Principal;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("CUSTOMER"); 
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }

    @GetMapping("/account")
    public String viewAccount(Model model, Principal principal) {
    User user = userRepository.findByUsername(principal.getName()).orElseThrow();
    model.addAttribute("user", user);
    return "account";
    }

    @PostMapping("/account")
    public String updateAccount(@ModelAttribute("user") User updatedUser,
    @RequestParam(required = false) String newPassword,
    Principal principal) {
    User existing = userRepository.findByUsername(principal.getName()).orElseThrow();

    existing.setShippingAddress(updatedUser.getShippingAddress());
    existing.setPaymentMethod(updatedUser.getPaymentMethod());

    if (newPassword != null && !newPassword.isBlank()) {
        existing.setPassword(passwordEncoder.encode(newPassword));
    }
    userRepository.save(existing);
    return "redirect:/account?updated=true";
}



}
