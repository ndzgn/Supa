package iut.tp.sale;

import iut.tp.customer.Customer;
import iut.tp.exceptions.InsufficientStockException;
import iut.tp.exceptions.ProductNotFoundException;
import iut.tp.product.Product;
import iut.tp.stock.StockService;
import iut.tp.strategy.StandardTaxStrategy;
import iut.tp.strategy.TaxStrategy;

import java.util.List;

public class SaleService {
    private SaleRepository saleRepository;
    private StockService stockService;
    private TaxStrategy taxStrategy;

    public SaleService(SaleRepository saleRepository, StockService stockService) {
        this.saleRepository = saleRepository;
        this.stockService = stockService;
        this.taxStrategy = new StandardTaxStrategy();
    }

    public void setTaxStrategy(TaxStrategy taxStrategy) {
        this.taxStrategy = taxStrategy;
    }

    public Sale createSale(String saleId, Customer customer) {
        return new Sale(saleId, customer);
    }

    public void addItemToSale(Sale sale, Product product, int quantity)
            throws InsufficientStockException, ProductNotFoundException {

        int available = stockService.getAvailableQuantity(product.getId());
        if (available < quantity) {
            throw new InsufficientStockException(
                    "Stock insuffisant. Disponible: " + available);
        }

        SaleItem item = new SaleItem(product, quantity, product.getPrice());
        sale.addItem(item);
    }

    public void completeSale(Sale sale) throws ProductNotFoundException, InsufficientStockException {
        for (SaleItem item : sale.getItems()) {
            stockService.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }
        sale.calculateTotal(taxStrategy);
        saleRepository.save(sale);
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Sale getSaleById(String id) {
        return saleRepository.findById(id);
    }

    public List<Sale> getSalesByCustomer(String customerId) {
        return saleRepository.findByCustomerId(customerId);
    }

    public TaxStrategy getTaxStrategy() {
        return taxStrategy;
    }
}
