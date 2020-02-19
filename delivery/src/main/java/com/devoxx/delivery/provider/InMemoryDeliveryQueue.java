package com.devoxx.delivery.provider;

import com.devoxx.delivery.domain.DeliveryQueue;
import com.devoxx.delivery.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class InMemoryDeliveryQueue implements DeliveryQueue {
    private List<Order> ordersInProcess = new ArrayList<>();

    @Override
    public void addInProcessOrder(Order order) {
        ordersInProcess.add(order);
    }

    @Override
    public List<Order> ordersInProcess() {
        return Collections.unmodifiableList(ordersInProcess);
    }

    @Override
    public void clear() {
        ordersInProcess.clear();
    }


}
