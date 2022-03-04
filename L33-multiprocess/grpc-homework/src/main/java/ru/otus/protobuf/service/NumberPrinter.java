package ru.otus.protobuf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aleksandr Semykin
 */
public class NumberPrinter {

    private static final Logger log = LoggerFactory.getLogger(NumberPrinter.class);

    private volatile long serverValue;

    public void onNext(long value) {
        serverValue = value;
    }

    public void startPrinting() {
        long currentValue = 0;
        for (long i = 0; i <= 50; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                currentValue = currentValue + 1 + serverValue;
                serverValue = 0;
            }
            log.info("currentValue: {}", currentValue);
        }
    }
}
