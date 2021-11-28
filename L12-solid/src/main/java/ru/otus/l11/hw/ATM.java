package ru.otus.l11.hw;

import java.util.List;

public interface ATM {

    void enterCash(List<Bill> bills);

    List<Bill> withdrawCash(int amount);

    int getBalance();
}
