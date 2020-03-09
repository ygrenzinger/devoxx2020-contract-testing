package com.devoxx.checkout.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class Cashier {
    private final Inventory inventory;
    private final Delivery delivery;

    public Cashier(Inventory inventory, Delivery delivery) {
        this.inventory = inventory;
        this.delivery = delivery;
    }

    public ValidatedOrder checkoutNow(Order order) {
        ValidatedOrder validatedOrder = checkStock(order);
        delivery.send(validatedOrder);
        return validatedOrder;
    }

    private ValidatedOrder checkStock(Order order) {
        int remainingStock = this.inventory
                .retrieveBook(order.getBookId()).getStock();
        if (remainingStock < order.getQuantity()) {
            throw new NoMoreStockException();
        }
        return ValidatedOrder.from(order, LocalDateTime.now());
    }
}
