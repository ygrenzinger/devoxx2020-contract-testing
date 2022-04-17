package com.devoxx.checkout;

import com.devoxx.checkout.domain.Book;
import com.devoxx.checkout.domain.Inventory;
import com.devoxx.checkout.domain.Order;
import com.devoxx.checkout.domain.ValidatedOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureStubRunner
@Import({TestChannelBinderConfiguration.class})
public class CheckoutApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OutputDestination output;

    @Autowired
    private Inventory inventory;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_checkout_order() throws Exception {
        //when
        Order order = new Order("d4d37e73-77a0-4616-8bd2-5ed983d45d14", 2, "yannick");
        mvc.perform(
                post("/v1/checkouts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order))
        ).andExpect(status().isOk());


        //then
        ValidatedOrder payload = objectMapper.readValue(output.receive().getPayload(), ValidatedOrder.class);
        assertThat(payload.getBookId()).isEqualTo("d4d37e73-77a0-4616-8bd2-5ed983d45d14");
        assertThat(payload.getQuantity()).isEqualTo(2);
        assertThat(payload.getClientId()).isEqualTo("yannick");
        assertThat(payload.getCreatedAt()).isNotNull();
    }

    @Test
    void should_retrieve_book() {
        //when
        Book book = inventory.retrieveBook("d4d37e73-77a0-4616-8bd2-5ed983d45d14");
        assertThat(book.getStock()).isEqualTo(100);

    }

}

