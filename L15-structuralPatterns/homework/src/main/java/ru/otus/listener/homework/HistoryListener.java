package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long, Message> messagesQueues = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        messagesQueues.put(msg.getId(), new Message(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messagesQueues.get(id));
    }
}
