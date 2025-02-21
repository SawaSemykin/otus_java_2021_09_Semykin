package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

/**
 * @author Aleksandr Semykin
 */
public class ProcessorSwapField11AndField12 implements Processor {

    @Override
    public Message process(Message message) {
            return new Message(message).toBuilder()
                    .field11(message.getField12())
                    .field12(message.getField11())
                    .build();
    }
}
