package com.devoxx.delivery.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Order {
    private final String bookId;
    private final int quantity;
    private final String clientId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Order(@JsonProperty("bookId") String bookId, @JsonProperty("quantity") int quantity,  @JsonProperty("clientId") String clientId) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.clientId = clientId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return quantity == order.quantity &&
                bookId.equals(order.bookId) &&
                clientId.equals(order.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, quantity, clientId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "bookId='" + bookId + '\'' +
                ", quantity=" + quantity +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}

