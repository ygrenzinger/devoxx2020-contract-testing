package com.devoxx.delivery;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.devoxx.delivery.domain.DeliveryQueue;
import com.devoxx.delivery.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@PactTestFor(providerName = "checkout-service", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
class DeliveryApplicationTests {

    @Autowired
    private DeliveryQueue deliveryQueue;

    @BeforeEach
    void before() {
        deliveryQueue.clear();
    }

    @Pact(consumer = "delivery-service")
    V4Pact orderContract(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody()
                .stringType("bookId", "d4d37e73-77a0-4616-8bd2-5ed983d45d14")
                .numberType("quantity", 10)
                .stringType("clientId","yannick");

        return builder.given("an order is sent")
                .expectsToReceive("an order")
                .withContent(body)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "orderContract")
    void should_correctly_process_order(V4Interaction.AsynchronousMessage message) {
        publishMessageToQueue(message);

        Order expectedOrder = new Order("d4d37e73-77a0-4616-8bd2-5ed983d45d14", 10, "yannick");
        assertThat(deliveryQueue.ordersInProcess()).containsExactly(expectedOrder);
    }

    private void publishMessageToQueue(V4Interaction.AsynchronousMessage messagePact) {
        Message<byte[]> msg = MessageBuilder.withPayload(messagePact.asMessage().contentsAsBytes()).build();

    }
}
