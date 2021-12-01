package ru.otus.l11.hw;

import lombok.Getter;

public enum BillValue {
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    FIVE_THOUSAND(5000);

    @Getter
    private final int value;

    BillValue(int value) {
        this.value = value;
    }
}
