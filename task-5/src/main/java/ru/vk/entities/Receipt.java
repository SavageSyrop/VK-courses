package ru.vk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Receipt {
    private Long id;
    private LocalDate creationDate;
    private Organisation organisation;
    private List<ReceiptItem> items;

    public Receipt(LocalDate creationDate, Organisation organisation) {
        this.creationDate = creationDate;
        this.organisation = organisation;
    }
}
