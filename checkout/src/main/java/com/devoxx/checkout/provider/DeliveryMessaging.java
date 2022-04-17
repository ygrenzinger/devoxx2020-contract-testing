package com.devoxx.checkout.provider;

import com.devoxx.checkout.domain.Delivery;
import com.devoxx.checkout.domain.ValidatedOrder;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMessaging implements Delivery {

    private final StreamBridge streamBridge;

    public DeliveryMessaging(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void send(ValidatedOrder order) {
        streamBridge.send("orders", order);
    }
}
