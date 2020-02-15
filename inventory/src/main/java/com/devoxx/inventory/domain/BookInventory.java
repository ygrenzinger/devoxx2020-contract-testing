package com.devoxx.inventory.domain;

public interface BookInventory {
    Iterable<Book> allBooks(boolean onlyHasStock);

    Book findBook(String bookId);

    void insertBook(Book book);

    void removeAllStocks();

    Book reduceStock(String bookId, Integer total);

    Book increaseStock(String bookId, Integer total);
}
