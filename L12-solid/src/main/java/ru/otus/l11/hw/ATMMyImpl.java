package ru.otus.l11.hw;

import java.util.*;

/**
 * @author Aleksandr Semykin
 */
public class ATMMyImpl implements ATM {

    private final CashHolder cashHolder;

    public ATMMyImpl(CashHolder cashHolder) {
        this.cashHolder = cashHolder;
    }

    @Override
    public void enterCash(List<Bill> bills) {
        cashHolder.addCash(bills);
    }

    @Override
    public List<Bill> withdrawCash(int amount) {
        return cashHolder.getCash(amount);
    }

    @Override
    public int getBalance() {
        return cashHolder.getBalance();
    }
}
