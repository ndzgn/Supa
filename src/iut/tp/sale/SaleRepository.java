package iut.tp.sale;

import iut.tp.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SaleRepository implements Repository<Sale> {
    private Map<String, Sale> sales = new HashMap<>();

    @Override
    public void save(Sale sale) {
        sales.put(sale.getId(), sale);
    }

    @Override
    public Sale findById(String id) {
        return sales.get(id);
    }

    @Override
    public List<Sale> findAll() {
        return new ArrayList<>(sales.values());
    }

    @Override
    public void update(Sale sale) {
        sales.put(sale.getId(), sale);
    }

    @Override
    public void delete(String id) {
        sales.remove(id);
    }

    public List<Sale> findByCustomerId(String customerId) {
        return sales.values().stream()
                .filter(s -> s.getCustomer() != null && s.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());
    }
}
