package ru.otus.protobuf.model;

/**
 * @author Aleksandr Semykin
 */
public class Number {
    private long value;

    public Number(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
