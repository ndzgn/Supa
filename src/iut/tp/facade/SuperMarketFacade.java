package iut.tp.facade;

import iut.tp.customer.CustomerRepository;
import iut.tp.customer.CustomerService;
import iut.tp.observer.StockAlertNotifier;
import iut.tp.product.ProductRepository;
import iut.tp.product.ProductService;
import iut.tp.sale.SaleRepository;
import iut.tp.sale.SaleService;
import iut.tp.stock.StockRepository;
import iut.tp.stock.StockService;
import iut.tp.supply.SupplyRepository;
import iut.tp.supply.SupplyService;

public class SuperMarketFacade {
    private ProductService productService;
    private StockService stockService;
    private SaleService saleService;
    private SupplyService supplyService;
    private CustomerService customerService;

    public SuperMarketFacade() {
        ProductRepository productRepo = new ProductRepository();
        StockRepository stockRepo = new StockRepository();
        SaleRepository saleRepo = new SaleRepository();
        SupplyRepository supplyRepo = new SupplyRepository();
        CustomerRepository customerRepo = new CustomerRepository();

        this.productService = new ProductService(productRepo);
        this.stockService = new StockService(stockRepo);
        this.saleService = new SaleService(saleRepo, stockService);
        this.supplyService = new SupplyService(supplyRepo, stockService);
        this.customerService = new CustomerService(customerRepo);

        stockService.addObserver(new StockAlertNotifier());
    }

    public ProductService getProductService() { return productService; }
    public StockService getStockService() { return stockService; }
    public SaleService getSaleService() { return saleService; }
    public SupplyService getSupplyService() { return supplyService; }
    public CustomerService getCustomerService() { return customerService; }
}
