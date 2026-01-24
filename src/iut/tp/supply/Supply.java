package iut.tp.supply;

import iut.tp.product.Product;

import java.time.LocalDateTime;

public class Supply {
    private String id;
    private Product product;
    private int quantity;
    private LocalDateTime supplyDate;
    private String supplier;

    public Supply(String id, Product product, int quantity, String supplier) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.supplier = supplier;
        this.supplyDate = LocalDateTime.now();
    }

    public String getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public LocalDateTime getSupplyDate() { return supplyDate; }
    public String getSupplier() { return supplier; }
}
