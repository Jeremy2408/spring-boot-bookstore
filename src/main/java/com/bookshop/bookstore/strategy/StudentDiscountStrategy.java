package com.bookshop.bookstore.strategy;

public class StudentDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double total) {
        return total * 0.85; 
    }
}
