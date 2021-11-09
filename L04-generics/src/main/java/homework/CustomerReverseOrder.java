package homework;


import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private Deque<Customer> deque;

    public CustomerReverseOrder() {
        deque = new ArrayDeque<>();
    }

    public void add(Customer customer) {
        deque.push(customer);
    }

    public Customer take() {
        return deque.poll();
    }
}
