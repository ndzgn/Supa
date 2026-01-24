package iut.tp.customer;

import iut.tp.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRepository implements Repository<Customer> {
    private Map<String, Customer> customers = new HashMap<>();

    @Override
    public void save(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    @Override
    public Customer findById(String id) {
        return customers.get(id);
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public void update(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    @Override
    public void delete(String id) {
        customers.remove(id);
    }
}
