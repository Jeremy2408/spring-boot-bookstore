package com.bookshop.bookstore.strategy;

public class LoyaltyDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double total) {
        return total * 0.9; 
    }
}
