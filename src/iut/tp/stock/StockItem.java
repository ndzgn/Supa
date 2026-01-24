package iut.tp.stock;


import iut.tp.exceptions.InsufficientStockException;
import iut.tp.product.Product;

public class StockItem {
    private Product product;
    private int quantity;
    private int minThreshold;

    public StockItem(Product product, int quantity, int minThreshold) {
        this.product = product;
        this.quantity = quantity;
        this.minThreshold = minThreshold;
    }

    public boolean isLowStock() {
        return quantity <= minThreshold;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public void reduceQuantity(int amount) throws InsufficientStockException {
        if (quantity < amount) {
            throw new InsufficientStockException("Stock insuffisant pour " + product.getName());
        }
        this.quantity -= amount;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public int getMinThreshold() { return minThreshold; }
}