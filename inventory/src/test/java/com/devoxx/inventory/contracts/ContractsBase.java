package com.devoxx.inventory.contracts;

import com.devoxx.inventory.api.BookController;
import com.devoxx.inventory.domain.Book;
import com.devoxx.inventory.domain.BookIdGenerator;
import com.devoxx.inventory.infrastructure.InMemoryBookInventory;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ContractsBase {

    @Autowired
    private InMemoryBookInventory bookInventory;

    @Autowired
    private BookIdGenerator bookIdGenerator;

    @BeforeEach
    public void before() {
        bookInventory.removeAllStocks();
        Book java = new Book("d4d37e73-77a0-4616-8bd2-5ed983d45d14", "Java", BigDecimal.valueOf(19.9), 100);
        Book kotlin = new Book("8364948b-6221-4cd8-9fd9-db0d17d45ef8", "Kotlin", BigDecimal.valueOf(22.4), 0);
        bookInventory.insertBook(java);
        bookInventory.insertBook(kotlin);

        // config rest assured
        RestAssuredMockMvc.standaloneSetup(new BookController(bookInventory, bookIdGenerator));
    }

}
