package com.devoxx.inventory.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class Book {
    private String id;
    private final String name;
    private final BigDecimal price;
    private final int stock;

    public Book(String id, String name, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Book(@JsonProperty("name") String name, @JsonProperty("price") BigDecimal price, @JsonProperty("stock") int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void initUUID(String id) {
        this.id = id;
    }

    public Book changeStock(int stock) {
        return new Book(id, name, price, stock);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return stock == book.stock &&
                id.equals(book.id) &&
                name.equals(book.name) &&
                price.equals(book.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, stock);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
