package ru.otus.l11.hw;

import java.util.*;

/**
 * @author Aleksandr Semykin
 */
public class ATMMyImpl implements ATM {
    private final static Comparator<Bill.Value> largeBillsFirst = Comparator.comparing(Bill.Value::getValue).reversed();

    private final Map<Bill.Value, Integer> bank = new TreeMap<>(largeBillsFirst);

    @Override
    public void enterCash(List<Bill> bills) {
        for (Bill bill : bills) {
            bank.merge(bill.getValue(), 1, (oldValue, newValue) -> oldValue + 1);
        }
    }

    @Override
    public List<Bill> withdrawCash(int amount) {
        var billsMap = validateWithdrawAndFindBills(amount);
        withdrawCash(billsMap);
        return collectBills(billsMap);
    }

    @Override
    public int getBalance() {
        return bank.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue())
                .sum();
    }

    private TreeMap<Bill.Value, Integer> validateWithdrawAndFindBills(int amount) {
        if (isTotalBalanceExceeded(amount)) {
            throw new RuntimeException("Amount " + amount + "exceeds total balance");
        }

        var billsToWithdraw = new TreeMap<Bill.Value, Integer>(largeBillsFirst);
        for (var entry : bank.entrySet()) {
            int billValue = entry.getKey().getValue();
            int needCount = amount / billValue;
            int availableCount = entry.getValue();
            if (needCount > availableCount) {
                needCount = availableCount;
            }
            billsToWithdraw.put(entry.getKey(), needCount);
            amount -= billValue * needCount;
        }

        if (amount != 0) {
            throw new RuntimeException("The ATM ran out of bills. Try another amount");
        }

        return billsToWithdraw;
    }

    private void withdrawCash(Map<Bill.Value, Integer> cash) {
        for (var entry : cash.entrySet()) {
            bank.computeIfPresent(entry.getKey(), (key, currValue) -> currValue - entry.getValue());
        }
    }

    private ArrayList<Bill> collectBills(Map<Bill.Value, Integer> billsMap) {
        var billsList = new ArrayList<Bill>();
        for (var entry : billsMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                billsList.add(new Bill(entry.getKey()));
            }
        }
        return billsList;
    }

    private boolean isTotalBalanceExceeded(int amount) {
        return amount - getBalance() > 0;
    }
}
