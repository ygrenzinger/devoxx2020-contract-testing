package com.devoxx.checkout.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidatedOrder {
    private String bookId;
    private int quantity;
    private String clientId;
    private LocalDateTime createdAt;

    public static ValidatedOrder from(Order order, LocalDateTime now) {
        return new ValidatedOrder(
                order.getBookId(), order.getQuantity(), order.getClientId(), now.truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
