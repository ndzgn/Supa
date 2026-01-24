package iut.tp.customer;

import java.util.List;

public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createCustomer(String id, String name, String phone, String email) {
        Customer customer = new Customer(id, name, phone, email);
        customerRepository.save(customer);
    }

    public Customer getCustomer(String id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
