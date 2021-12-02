package ru.otus.l11.hw;

/**
 * @author Aleksandr Semykin
 */
public class ATMFactory {
    public static ATM createATM(CashHolder cashHolder) {
        return new ATMMyImpl(cashHolder);
    }
}
