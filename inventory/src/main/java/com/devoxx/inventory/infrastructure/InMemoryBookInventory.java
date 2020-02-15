package com.devoxx.inventory.infrastructure;

import com.devoxx.inventory.domain.Book;
import com.devoxx.inventory.domain.BookInventory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

@Repository
public class InMemoryBookInventory implements BookInventory {

    private final Map<String, Book> inventory = new HashMap<>();

    @Override
    public Iterable<Book> allBooks() {
        return inventory.values();
    }

    @Override
    public Book findBook(String bookId) {
        return inventory.get(bookId);
    }

    @Override
    public void insertBook(Book book) {
        inventory.put(book.getId(), book);
    }

    @Override
    public void removeAllStocks() {
        inventory.clear();
    }

    @Override
    public Book reduceStock(String bookId, Integer number) {
        return changeStock(bookId, stock -> stock - number);
    }

    @Override
    public Book increaseStock(String bookId, Integer number) {
        return changeStock(bookId, stock -> stock - number);
    }

    private Book changeStock(String bookId, IntFunction<Integer> fn) {
        Book book = inventory.get(bookId);
        int remaining = fn.apply(book.getStock());
        book.setStock(remaining);
        return book;
    }
}
