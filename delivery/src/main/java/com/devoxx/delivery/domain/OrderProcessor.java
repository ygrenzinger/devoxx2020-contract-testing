package com.devoxx.delivery.domain;

import org.springframework.stereotype.Component;

@Component
public class OrderProcessor {
    private final DeliveryQueue deliveryQueue;

    public OrderProcessor(DeliveryQueue deliveryQueue) {
        this.deliveryQueue = deliveryQueue;
    }

    public void processOrder(Order order) {
        deliveryQueue.addInProcessOrder(order);
    }
}
