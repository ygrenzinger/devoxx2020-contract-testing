package com.devoxx.delivery;

import com.devoxx.delivery.domain.DeliveryQueue;
import com.devoxx.delivery.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureStubRunner(
        ids = {"com.devoxx:checkout"},
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
class DeliveryApplicationTests {

    @Autowired
    StubTrigger stubTrigger;

    @Autowired
    private DeliveryQueue deliveryQueue;

    @BeforeEach
    void before() {
        deliveryQueue.clear();
    }

    @Test
    void should_correctly_process_order() {
        stubTrigger.trigger("should send order");
        Order order = new Order("d4d37e73-77a0-4616-8bd2-5ed983d45d14", 10, "yannick");
        assertThat(deliveryQueue.ordersInProcess()).containsExactly(order);
    }

}
