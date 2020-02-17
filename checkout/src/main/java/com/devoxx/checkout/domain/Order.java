package com.devoxx.checkout.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String bookId;
    private int number;
    private String clientId;
    private LocalDateTime createdAt;
}
