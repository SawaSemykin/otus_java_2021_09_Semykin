package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Aleksandr Semykin
 */
public class Solution {

    private static final Logger log = LoggerFactory.getLogger(Solution.class);

    private final static String THREAD_ONE_NAME = "Thread1";
    private final static String THREAD_TWO_NAME = "Thread2";
    private final static int MIN = 1;
    private final static int MAX = 10;

    private final Deque<Integer> numbers = new ArrayDeque<>();

    {
        fillNumbers();
    }

    private synchronized void action() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (THREAD_ONE_NAME.equals(Thread.currentThread().getName())) {
                    log.info("{}: {}", THREAD_ONE_NAME, numbers.peek());
                } else if (THREAD_TWO_NAME.equals(Thread.currentThread().getName())) {
                    int number = numbers.poll();
                    log.info("{}: {}", THREAD_TWO_NAME, number);
                    if (numbers.isEmpty() && number == MAX) {
                        fillNumbersReversed();
                    } else if (numbers.isEmpty() && number == MIN + 1) {
                        fillNumbers();
                    }
                }
                this.notifyAll();
                this.wait(1000);

                Thread.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private void fillNumbers() {
        numbers.addAll(
                IntStream.range(MIN, MAX + 1)
                        .boxed()
                        .collect(Collectors.toList())
        );
    }

    private void fillNumbersReversed() {
        numbers.addAll(
                IntStream.range(MIN + 1, MAX)
                        .boxed()
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList())
        );
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        new Thread(null, solution::action, THREAD_ONE_NAME).start();
        new Thread(null, solution::action, THREAD_TWO_NAME).start();
    }
}
