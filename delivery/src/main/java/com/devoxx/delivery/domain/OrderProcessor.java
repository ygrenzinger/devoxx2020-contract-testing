package com.devoxx.delivery.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderProcessor {
    private final DeliveryQueue deliveryQueue;

    public OrderProcessor(DeliveryQueue deliveryQueue) {
        this.deliveryQueue = deliveryQueue;
    }

    public void processOrder(Order order) {
        log.info("Adding order in process {}", order);
        deliveryQueue.addInProcessOrder(order);
    }
}
