package ru.otus.l11.hw;

import java.util.*;

/**
 * @author Aleksandr Semykin
 */
public class CashHolder {
    private final static Comparator<BillValue> largeBillsFirst = Comparator.comparing(BillValue::getValue).reversed();

    private final Map<BillValue, Integer> bank = new TreeMap<>(largeBillsFirst);

    public CashHolder() {}

    public CashHolder(List<Bill> cash) {
        addCash(cash);
    }

    public void addCash(List<Bill> cash) {
        for (Bill bill : cash) {
            bank.merge(bill.getValue(), 1, (oldValue, newValue) -> oldValue + 1);
        }
    }

    public List<Bill> getCash(int amount) {
        var billsMap = validateAndWithdrawCash(amount);
        return collectBills(billsMap);
    }

    public int getBalance() {
        return bank.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue())
                .sum();
    }

    private TreeMap<BillValue, Integer> validateAndWithdrawCash(int amount) {
        if (isTotalBalanceExceeded(amount)) {
            throw new RuntimeException("Amount " + amount + "exceeds total balance");
        }

        var billsToWithdraw = new TreeMap<BillValue, Integer>(largeBillsFirst);
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

        for (var entry : billsToWithdraw.entrySet()) {
            bank.computeIfPresent(entry.getKey(), (key, currValue) -> currValue - entry.getValue());
        }

        return billsToWithdraw;
    }

    private boolean isTotalBalanceExceeded(int amount) {
        return amount - getBalance() > 0;
    }


    private ArrayList<Bill> collectBills(Map<BillValue, Integer> billsMap) {
        var billsList = new ArrayList<Bill>();
        for (var entry : billsMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                billsList.add(new Bill(entry.getKey()));
            }
        }
        return billsList;
    }
}
