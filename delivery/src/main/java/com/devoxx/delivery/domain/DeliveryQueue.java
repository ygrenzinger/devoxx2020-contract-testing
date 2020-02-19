package com.devoxx.delivery.domain;

import java.util.List;

public interface DeliveryQueue {
    void addInProcessOrder(Order order);

    List<Order> ordersInProcess();

    void clear();
}
