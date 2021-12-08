package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorThrowExceptionOnEvenSecondTest {
    private DateTimeProvider dateTimeProvider;
    private ProcessorThrowExceptionOnEvenSecond processor;
    private Message msg;

    @BeforeEach
    void init() {
        dateTimeProvider = Mockito.mock(DateTimeProvider.class);
        processor = new ProcessorThrowExceptionOnEvenSecond(dateTimeProvider);
        msg = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();
    }

    @Test
    void processOnEvenSecondTest() {
        Mockito.doReturn(LocalDateTime.now().withSecond(2)).when(dateTimeProvider).getDate();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> processor.process(msg));

        assertEquals("It's even second", thrown.getMessage());
    }

    @Test
    void processOnOddSecondTest() {
        Mockito.doReturn(LocalDateTime.now().withSecond(3)).when(dateTimeProvider).getDate();

        Message processedMsg = processor.process(msg);

        assertEquals(msg, processedMsg);
        assertNotSame(msg, processedMsg);
    }
}