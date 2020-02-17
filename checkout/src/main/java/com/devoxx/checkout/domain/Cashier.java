package com.devoxx.checkout.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class Cashier {
    private final Inventory inventory;
    private final Delivery delivery;

    public Cashier(Inventory inventory, Delivery delivery) {
        this.inventory = inventory;
        this.delivery = delivery;
    }

    public Order checkoutNow(Order order) {
        order.setDate(LocalDateTime.now());
        checkStock(order);
        delivery.send(order);
        return order;
    }

    private void checkStock(Order order) {
        int remainingStock = this.inventory
                .retrieveBook(order.getBookId()).getStock();
        if (remainingStock < order.getNumber()) {
            throw new NoMoreStockException();
        }
    }
}
