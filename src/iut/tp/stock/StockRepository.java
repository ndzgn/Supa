package iut.tp.stock;

import iut.tp.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StockRepository  {
    private Map<String, StockItem> stockItems = new HashMap<>();

    public void save(StockItem item) {
        stockItems.put(item.getProduct().getId(), item);
    }

    public StockItem findByProductId(String productId) {
        return stockItems.get(productId);
    }

    public List<StockItem> findAll() {
        return new ArrayList<>(stockItems.values());
    }

    public List<StockItem> findLowStock() {
        return stockItems.values().stream()
                .filter(StockItem::isLowStock)
                .collect(Collectors.toList());
    }
}
