package fr.elecmove.api.controller.dto.payment;

import java.time.LocalDateTime;

public class PaymentSingleDTO {

    private String id;
    private Double amount;
    private LocalDateTime createdAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
