package com.bookshop.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BookOrder order;

    @ManyToOne
    private Book book;

    private int quantity;

    private double priceAtPurchase;
}
