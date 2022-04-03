package com.devoxx.inventory.contracts;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.devoxx.inventory.domain.Book;
import com.devoxx.inventory.domain.BookInventory;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("inventory-service")
@PactBroker
public class ContractVerificationTest {

    @MockBean
    private BookInventory bookInventory;

    @State("A product")
    void givenAProduct() {
        when(bookInventory.findBook(any())).thenReturn(
                new Book("ac9ab4d7-eaea-49a1-af1e-36b5acc29584",
                        "Clean Code",
                        BigDecimal.valueOf(39.99),
                        5)
        );
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

}
