package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorSwapField11AndField12Test {
    private ProcessorSwapField11AndField12 processor;
    private Message msg;


    @BeforeEach
    void init() {
        processor = new ProcessorSwapField11AndField12();
        msg = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .build();
    }

    @Test
    void processTest() {
        Message processedMsg = processor.process(msg);

        assertNotSame(msg, processedMsg);
        assertEquals(msg.getField11(), processedMsg.getField12());
        assertEquals(msg.getField12(), processedMsg.getField11());
    }
}
