package ru.otus.l11.hw;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author Aleksandr Semykin
 */
@lombok.Value
public class Bill {

    Value value;

    public enum Value {
        ONE_HUNDRED(100),
        FIVE_HUNDRED(500),
        ONE_THOUSAND(1000),
        FIVE_THOUSAND(5000);

        @Getter
        private final int value;

        Value(int value) {
            this.value = value;
        }
    }
}
