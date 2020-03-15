package com.devoxx.inventory.api;

import com.devoxx.inventory.domain.Book;
import com.devoxx.inventory.domain.BookIdGenerator;
import com.devoxx.inventory.domain.BookInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<Book> create(@RequestBody Book book) {
        if (StringUtils.isEmpty(book.getName())) {
            return ResponseEntity.badRequest().body(book);
        }

        book.initUUID(bookIdGenerator.randomId());
        bookInventory.insertBook(book);
        return ResponseEntity.created(URI.create("/v1/books/"+book.getId())).body(book);
    }

    @PostMapping("/v1/books/{id}/stock/reduce/{number}")
    public Book reduceStock(@PathVariable String id, @PathVariable Integer number) {
        return bookInventory.reduceStock(id, number);
    }

}
