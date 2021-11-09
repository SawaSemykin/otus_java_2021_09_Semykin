package ru.otus.hw;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Aleksandr Semykin
 */
public class MyDeque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    private class Node {
        Item item;
        Node prev, next;
    }

    // construct an empty deque
    public MyDeque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateInput(item);
        Node oldNew = first;
        first = new Node();
        first.item = item;
        if (oldNew == null) last = first;
        else {
            oldNew.prev = first;
            first.next = oldNew;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        validateInput(item);
        Node oldLast = last;
        last = new Node();
        last.item = item;
        if (oldLast == null) first = last;
        else {
            oldLast.next = last;
            last.prev = oldLast;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        throwExceptionOnEmptyData();
        Item item = first.item;
        first = first.next;
        first.prev = null;
        size--;
        return item;
    }

    /**
     * For demonstration purpose of my test framework
     * @return
     */
    public Item removeFirstWrongImpl() {
        return removeLast();
    }

    // remove and return the item from the back
    public Item removeLast() {
        throwExceptionOnEmptyData();
        Item item = last.item;
        last = last.prev;
        last.next = null;
        size--;
        return item;
    }

    public Item peekFirst() {
        return first.item;
    }

    public Item peekLast() {
        return last.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("There is no more element");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void validateInput(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Passing null is prohibited!");
    }

    private void throwExceptionOnEmptyData() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty!");
    }
}
