package ru.otus.l11.hw;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static ru.otus.l11.hw.Bill.Value.*;


class ATMMyImplTest {
    private ATM atm;

    @BeforeEach
    void setup() {
        atm = new ATMMyImpl();
    }

    @Test
    void givenEmptyBalance_whenEnterOneBillOf1000_thenGetBalance1000() {
        enterCash(new Bill(ONE_THOUSAND));

        Assertions.assertThat(atm.getBalance()).isEqualTo(ONE_THOUSAND.getValue());
    }


    @Test
    void givenEmptyBalance_whenEnterOneBillOf1000AndTwoBillsOf500_thenGetBalance2000() {
        enterCash(new Bill(ONE_THOUSAND), new Bill(FIVE_HUNDRED), new Bill(FIVE_HUNDRED));

        Assertions.assertThat(atm.getBalance()).isEqualTo(ONE_THOUSAND.getValue() + 2 * FIVE_HUNDRED.getValue());
    }

    @Test
    void givenEmptyBalance_whenEnterOneBillOfAllValues_thenGetBalance6600() {
        enterCash(new Bill(ONE_HUNDRED), new Bill(FIVE_HUNDRED), new Bill(ONE_THOUSAND), new Bill(FIVE_THOUSAND));

        Assertions.assertThat(atm.getBalance()).isEqualTo(ONE_HUNDRED.getValue() + FIVE_HUNDRED.getValue()
                + ONE_THOUSAND.getValue() + FIVE_THOUSAND.getValue());
    }

    @Test
    void givenBalance100_whenWithdraw200_thenExceptionThrown() {
        enterCash(new Bill(ONE_HUNDRED));

        Assertions.assertThatThrownBy(() -> atm.withdrawCash(200)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("exceeds");
    }

    @Test
    void givenBalance500AsSingleBill_whenWithdraw100_thenExceptionThrown() {
        enterCash(new Bill(FIVE_HUNDRED));

        Assertions.assertThatThrownBy(() -> atm.withdrawCash(100)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("The ATM ran out of bills");
    }

    @Test
    void givenBalance500AsSingle500Bill_whenWithdraw500_thenReturn500BillAndBalance0() {
        enterCash(new Bill(FIVE_HUNDRED));

        List<Bill> actualCash = atm.withdrawCash(500);

        Assertions.assertThat(actualCash).hasSize(1).allMatch(bill -> bill.getValue() == FIVE_HUNDRED);
        Assertions.assertThat(atm.getBalance()).isEqualTo(0);
    }

    @Test
    void givenBalance500AsFive100Bills_whenWithdraw500_thenReturnFive100BillsAndBalance0() {
        enterCash(new Bill(ONE_HUNDRED), new Bill(ONE_HUNDRED), new Bill(ONE_HUNDRED),
                new Bill(ONE_HUNDRED), new Bill(ONE_HUNDRED));

        List<Bill> actualCash = atm.withdrawCash(500);

        Assertions.assertThat(actualCash).hasSize(5).allMatch(bill -> bill.getValue() == ONE_HUNDRED);
        Assertions.assertThat(atm.getBalance()).isEqualTo(0);
    }

    @Test
    void givenEmptyBalance_whenGetBalance_thenReturn0() {
        int actualBalance = atm.getBalance();

        Assertions.assertThat(actualBalance).isEqualTo(0);
    }

    @Test
    void givenBalance100_whenGetBalance_thenReturn100() {
        enterCash(new Bill(ONE_HUNDRED));

        int actualBalance = atm.getBalance();

        Assertions.assertThat(actualBalance).isEqualTo(ONE_HUNDRED.getValue());
    }

    @Test
    void givenBalance600As100BillAnd500Bill_whenGetBalance_thenReturn600() {
        enterCash(new Bill(ONE_HUNDRED), new Bill(FIVE_HUNDRED));

        int actualBalance = atm.getBalance();

        Assertions.assertThat(actualBalance).isEqualTo(ONE_HUNDRED.getValue() + FIVE_HUNDRED.getValue());
    }

    private void enterCash(Bill... bills) {
        atm.enterCash(Arrays.asList(bills));
    }
}