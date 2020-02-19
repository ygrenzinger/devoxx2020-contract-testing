package com.devoxx.delivery.provider;


import com.devoxx.delivery.domain.Order;
import com.devoxx.delivery.domain.OrderProcessor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageListener {

    private final OrderProcessor orderProcessor;

    public OrderMessageListener(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @StreamListener(Streams.ORDERS)
    public void processOrder(Order order) {
        orderProcessor.processOrder(order);
    }
}
