package ru.otus.homework;

/**
 * @author Aleksandr Semykin
 */
public class TestLoggingImpl implements TestLogging {
    @Override
    public void calculation(int param1) {

    }

    @Log
    @Override
    public void calculation(int param1, int param2) {

    }

    @Log
    @Override
    public void calculation(int param1, String param2) {

    }

    @Override
    public void calculation(int param1, int param2, int param3) {

    }
}
