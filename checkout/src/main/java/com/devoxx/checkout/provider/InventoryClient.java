package com.devoxx.checkout.provider;

import com.devoxx.checkout.domain.Book;
import com.devoxx.checkout.domain.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient implements Inventory {
    private final RestTemplate restTemplate;

    @Autowired
    public InventoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Book retrieveBook(String bookId) {
        return restTemplate.getForObject("http://localhost:8080/v1/books/" + bookId, Book.class);
    }

}
