package com.devoxx.delivery.provider;

import com.devoxx.delivery.domain.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryClient implements Inventory {

    @Override
    public void increaseStock(String bookId) {

    }
}
