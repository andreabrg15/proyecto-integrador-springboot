package com.axity.curso.retailservice.listener;

import com.axity.curso.retailservice.event.StockChangedEvent;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
public class StockEventListener {

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleStockChanged(StockChangedEvent event) {

        System.out.println(
                "AUDITORÍA DE INVENTARIO" +
                "\nProducto: " + event.productName() + 
                "\nStock anterior: " + event.previousStock() +
                "\nStock nuevo: " + event.newStock()
        );
    }
}