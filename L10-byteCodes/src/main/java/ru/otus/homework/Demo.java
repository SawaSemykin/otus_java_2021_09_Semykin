package ru.otus.homework;

/**
 * @author Aleksandr Semykin
 */
public class Demo {
    public static void main(String[] args) {
        new Demo().action();
    }

    public void action() {
        TestLogging instance = Ioc.createInstance();
        instance.calculation(6);
        instance.calculation(6, 42);
        instance.calculation(6, "42");
        instance.calculation(6, 42, 84);
    }
}
