package com.bookshop.bookstore.repository;

import com.bookshop.bookstore.model.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<BookOrder, Long> {
    List<BookOrder> findByUsername(String username);
}
