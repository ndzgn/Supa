package iut.tp.product;

import iut.tp.exceptions.ProductNotFoundException;

import java.util.List;

public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(String id, String name, String category,
                              double price, String barcode) {
        Product product = new Product(id, name, category, price, barcode);
        productRepository.save(product);
    }

    public Product getProduct(String id) throws ProductNotFoundException {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new ProductNotFoundException("Produit non trouve: " + id);
        }
        return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void updateProduct(Product product) {
        productRepository.update(product);
    }

    public void deleteProduct(String id) {
        productRepository.delete(id);
    }
}
