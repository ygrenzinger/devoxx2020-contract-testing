package com.devoxx.checkout.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ValidatedOrder {
    private final String bookId;
    private final int quantity;
    private final String clientId;
    private final LocalDateTime createdAt;

    public ValidatedOrder(String bookId, int quantity, String clientId, LocalDateTime createdAt) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.clientId = clientId;
        this.createdAt = createdAt;
    }

    public static ValidatedOrder from(Order order, LocalDateTime now) {
        return new ValidatedOrder(
                order.getBookId(), order.getQuantity(), order.getClientId(), now.truncatedTo(ChronoUnit.SECONDS)
        );
    }

    public String getBookId() {
        return bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getClientId() {
        return clientId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidatedOrder that = (ValidatedOrder) o;
        return quantity == that.quantity &&
                bookId.equals(that.bookId) &&
                clientId.equals(that.clientId) &&
                createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, quantity, clientId, createdAt);
    }

    @Override
    public String toString() {
        return "ValidatedOrder{" +
                "bookId='" + bookId + '\'' +
                ", quantity=" + quantity +
                ", clientId='" + clientId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
