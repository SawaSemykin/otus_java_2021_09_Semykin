package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.Messages;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long, Deque<Message>> messagesQueues = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        var msgQueue = messagesQueues.getOrDefault(msg.getId(), new ArrayDeque<>());
        msgQueue.push(Messages.copy(msg));
        messagesQueues.putIfAbsent(msg.getId(), msgQueue);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messagesQueues.get(id)).map(Deque::peek);
    }
}
