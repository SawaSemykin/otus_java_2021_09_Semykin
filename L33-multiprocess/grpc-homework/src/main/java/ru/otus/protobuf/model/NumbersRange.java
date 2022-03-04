package ru.otus.protobuf.model;

/**
 * @author Aleksandr Semykin
 */
public class NumbersRange {
    private long firstValue;
    private long secondValue;

    public NumbersRange(long firstValue, long secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public long getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(long firstValue) {
        this.firstValue = firstValue;
    }

    public long getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(long secondValue) {
        this.secondValue = secondValue;
    }
}
