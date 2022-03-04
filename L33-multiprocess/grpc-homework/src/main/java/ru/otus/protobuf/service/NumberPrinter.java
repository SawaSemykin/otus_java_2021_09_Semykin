package ru.otus.protobuf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author Aleksandr Semykin
 */
public class NumberPrinter {

    private static final Logger log = LoggerFactory.getLogger(NumberPrinter.class);

    private final BlockingQueue<Long> queue = new ArrayBlockingQueue<>(1);

    public void onNext(long value) {
        boolean result = queue.offer(value);
        if (!result) {
            log.warn("value: {} has not been added to the queue", value);
        }
    }

    public void startPrinting() {
        long currentValue = 0;
        for (long i = 0; i <= 50; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentValue = currentValue + 1 + Optional.ofNullable(queue.poll()).orElse(0L);
            log.info("currentValue: {}", currentValue);
        }
    }
}
