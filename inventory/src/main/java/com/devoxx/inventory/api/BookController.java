package com.devoxx.inventory.api;

import com.devoxx.inventory.domain.Book;
import com.devoxx.inventory.domain.BookIdGenerator;
import com.devoxx.inventory.domain.BookInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Optional;

@RestController
public class BookController {

    private final BookInventory bookInventory;
    private final BookIdGenerator bookIdGenerator;

    @Autowired
    public BookController(BookInventory bookInventory, BookIdGenerator bookIdGenerator) {
        this.bookInventory = bookInventory;
        this.bookIdGenerator = bookIdGenerator;
    }

    @GetMapping("/v1/books")
    public Iterable<Book> books(@RequestParam(value = "only-has-stock", required = false) Optional<Boolean> onlyHasStock) {
        return bookInventory.allBooks(onlyHasStock.orElse(false));
    }

    @GetMapping("/v1/books/{id}")
    public Book book(@PathVariable String id) {
        return bookInventory.findBook(id);
    }

    @PostMapping("/v1/books")
    public Book create(@RequestBody Book book) {
        book.setId(bookIdGenerator.randomId());
        bookInventory.insertBook(book);
        return book;
    }

    @PostMapping("/v1/books/{id}/stock/reduce/{number}")
    public Book reduceStock(@PathVariable String id, @PathVariable Integer number) {
        return bookInventory.reduceStock(id, number);
    }

    @PostMapping("/v1/books/{id}/stock/increase/{number}")
    public Book increaseStock(@PathVariable String id, @PathVariable Integer number) {
        return bookInventory.increaseStock(id, number);
    }


}
