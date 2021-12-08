package ru.otus.model;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksandr Semykin
 */
public class Messages {

    public static Message copy(Message msg) {
        assert msg != null;
        var historyMsgField13 = new ObjectForMessage();
        Optional.of(msg)
                .map(Message::getField13)
                .map(ObjectForMessage::getData)
                .map(List::copyOf)
                .ifPresent(historyMsgField13::setData);
        return msg.toBuilder()
                .field13(historyMsgField13)
                .build();
    }
}
