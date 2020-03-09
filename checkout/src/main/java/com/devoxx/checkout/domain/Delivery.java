package com.devoxx.checkout.domain;

public interface Delivery {
    void send(ValidatedOrder order);
}
