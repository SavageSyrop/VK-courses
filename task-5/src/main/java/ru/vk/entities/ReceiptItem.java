package ru.vk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReceiptItem {
    private Long id;
    private Long receiptId;
    private Product product;
    private Integer price;
    private Integer amount;

    public ReceiptItem(Long receiptId, Product product, Integer price, Integer amount) {
        this.receiptId = receiptId;
        this.product = product;
        this.price = price;
        this.amount = amount;
    }
}
