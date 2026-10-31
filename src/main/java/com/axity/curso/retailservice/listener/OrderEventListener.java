package com.axity.curso.retailservice.listener;

import com.axity.curso.retailservice.event.OrderCreatedEvent;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
public class OrderEventListener {

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleOrderCreated(OrderCreatedEvent event) {

        System.out.println(
                "Confirmación de compra para el pedido: "
                        + event.orderId() +
                "\nTotal de la compra: " + event.total()
        );
    }
}