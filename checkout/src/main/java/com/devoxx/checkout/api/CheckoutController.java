package com.devoxx.checkout.api;

import com.devoxx.checkout.domain.Cashier;
import com.devoxx.checkout.domain.Order;
import com.devoxx.checkout.domain.ValidatedOrder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {

    private final Cashier cashier;

    public CheckoutController(Cashier cashier) {
        this.cashier = cashier;
    }

    @PostMapping("/v1/checkouts")
    public ValidatedOrder create(@RequestBody Order order) {
        return cashier.checkoutNow(order);
    }

}
