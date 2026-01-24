package iut.tp.stock;

import iut.tp.exceptions.InsufficientStockException;
import iut.tp.exceptions.ProductNotFoundException;
import iut.tp.observer.StockObserver;
import iut.tp.product.Product;

import java.util.ArrayList;
import java.util.List;

public class StockService {
    private StockRepository stockRepository;
    private List<StockObserver> observers = new ArrayList<>();

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void addStock(Product product, int quantity, int minThreshold) {
        StockItem item = new StockItem(product, quantity, minThreshold);
        stockRepository.save(item);
    }

    public void increaseStock(String productId, int quantity)
            throws ProductNotFoundException {
        StockItem item = stockRepository.findByProductId(productId);
        if (item == null) {
            throw new ProductNotFoundException("Produit non trouve dans le stock");
        }
        item.addQuantity(quantity);
    }

    public void decreaseStock(String productId, int quantity)
            throws ProductNotFoundException, InsufficientStockException {
        StockItem item = stockRepository.findByProductId(productId);
        if (item == null) {
            throw new ProductNotFoundException("Produit non trouve dans le stock");
        }
        item.reduceQuantity(quantity);

        if (item.isLowStock()) {
            notifyObservers(item);
        }
    }

    private void notifyObservers(StockItem item) {
        for (StockObserver observer : observers) {
            observer.onLowStock(item);
        }
    }

    public List<StockItem> getAllStock() {
        return stockRepository.findAll();
    }

    public List<StockItem> getLowStockItems() {
        return stockRepository.findLowStock();
    }

    public int getAvailableQuantity(String productId) {
        StockItem item = stockRepository.findByProductId(productId);
        return item != null ? item.getQuantity() : 0;
    }
}
