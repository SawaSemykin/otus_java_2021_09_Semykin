package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

/**
 * @author Aleksandr Semykin
 */
public class ProcessorThrowExceptionOnEvenSecond implements Processor {
    private final DateTimeProvider timeProvider;

    public ProcessorThrowExceptionOnEvenSecond(DateTimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        if (timeProvider.getDate().getSecond() % 2 == 0) {
            throw new RuntimeException("It's even second");
        }
        return new Message(message);
    }
}
