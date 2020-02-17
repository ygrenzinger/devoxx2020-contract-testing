package com.devoxx.checkout;

import com.devoxx.checkout.domain.Book;
import com.devoxx.checkout.domain.Inventory;
import com.devoxx.checkout.domain.Order;
import com.devoxx.checkout.provider.Streams;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureStubRunner(
        ids = {"com.devoxx:inventory:+:stubs:8080"},
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@AutoConfigureMockMvc
public class CheckoutApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private Streams streams;

    @Autowired
    private Inventory inventory;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Test
    void should_checkout_order() throws Exception {
        //when
        Order order = new Order("d4d37e73-77a0-4616-8bd2-5ed983d45d14", 2, "yannick", null);
        mvc.perform(
                post("/v1/checkouts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order))
        ).andExpect(status().isOk());

        //then
        Message<String> received = (Message<String>) messageCollector.forChannel(streams.orders()).poll();
        Order payload = objectMapper.readValue(Objects.requireNonNull(received).getPayload(), Order.class);
        assertThat(payload).isEqualToIgnoringGivenFields(order, "date");
    }

    @Test
    void should_retrieve_book() {
        //when
        Book book = inventory.retrieveBook("d4d37e73-77a0-4616-8bd2-5ed983d45d14");
        assertThat(book.getStock()).isEqualTo(100);

    }

}

