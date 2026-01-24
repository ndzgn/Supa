package iut.tp.sale;

import iut.tp.customer.Customer;
import iut.tp.strategy.TaxStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private String id;
    private Customer customer;
    private List<SaleItem> items;
    private LocalDateTime saleDate;
    private double subtotal;
    private double taxAmount;
    private double totalAmount;

    public Sale(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.saleDate = LocalDateTime.now();
    }

    public void addItem(SaleItem item) {
        items.add(item);
    }

    public void calculateTotal(TaxStrategy taxStrategy) {
        this.subtotal = items.stream()
                .mapToDouble(SaleItem::getSubtotal)
                .sum();
        this.taxAmount = subtotal * taxStrategy.getTaxRate();
        this.totalAmount = subtotal + taxAmount;
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public List<SaleItem> getItems() { return new ArrayList<>(items); }
    public LocalDateTime getSaleDate() { return saleDate; }
    public double getSubtotal() { return subtotal; }
    public double getTaxAmount() { return taxAmount; }
    public double getTotalAmount() { return totalAmount; }
}
