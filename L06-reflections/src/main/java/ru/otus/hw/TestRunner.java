package ru.otus.hw;

/**
 * @author Aleksandr Semykin
 */
public class TestRunner {
    public static void main(String[] args) {
        TestRunner.run("ru.otus.hw.MyDequeTest");
    }

    public static void run(String className) {
        try {
            new TestProcessor().process(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
