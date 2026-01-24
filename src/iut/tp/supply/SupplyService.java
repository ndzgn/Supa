package iut.tp.supply;

import iut.tp.exceptions.ProductNotFoundException;
import iut.tp.product.Product;
import iut.tp.stock.StockService;

import java.util.List;

public class SupplyService {
    private SupplyRepository supplyRepository;
    private StockService stockService;

    public SupplyService(SupplyRepository supplyRepository, StockService stockService) {
        this.supplyRepository = supplyRepository;
        this.stockService = stockService;
    }

    public void createSupply(String id, Product product, int quantity, String supplier)
            throws ProductNotFoundException {
        Supply supply = new Supply(id, product, quantity, supplier);
        supplyRepository.save(supply);
        stockService.increaseStock(product.getId(), quantity);
    }

    public List<Supply> getAllSupplies() {
        return supplyRepository.findAll();
    }
}
