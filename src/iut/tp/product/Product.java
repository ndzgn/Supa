package iut.tp.product;

public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private String barcode;

    public Product(String id, String name, String category, double price, String barcode) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.barcode = barcode;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getBarcode() { return barcode; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
}
