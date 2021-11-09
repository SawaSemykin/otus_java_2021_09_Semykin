package homework;


import java.util.*;

public class CustomerService {

    private NavigableMap<Customer, String> map;

    public CustomerService() {
        map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        Customer smallest = map.firstKey();
        Customer copy = new Customer(smallest.getId(), smallest.getName(), smallest.getScores());
        return Map.entry(copy, map.get(smallest));
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Customer next = map.higherKey(customer);
        return Optional.ofNullable(next)
                .map(e -> new Customer(e.getId(), e.getName(), e.getScores()))
                .map(e -> Map.entry(e, map.get(e)))
                .orElse(null);
    }

    public void add(Customer customer, String data) {
        Customer copy = new Customer(customer.getId(), customer.getName(), customer.getScores());
        map.put(copy, data);
    }
}
