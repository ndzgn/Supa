package iut.tp.product;

import iut.tp.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository implements Repository<Product> {
    private Map<String, Product> products = new HashMap<>();

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public Product findById(String id) {
        return products.get(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void update(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public void delete(String id) {
        products.remove(id);
    }

    public Product findByBarcode(String barcode) {
        return products.values().stream()
                .filter(p -> p.getBarcode().equals(barcode))
                .findFirst()
                .orElse(null);
    }
}
