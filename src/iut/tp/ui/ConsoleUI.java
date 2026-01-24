package iut.tp.ui;

import iut.tp.customer.Customer;
import iut.tp.exceptions.ProductNotFoundException;
import iut.tp.facade.SuperMarketFacade;
import iut.tp.product.Product;
import iut.tp.sale.Sale;
import iut.tp.sale.SaleItem;
import iut.tp.stock.StockItem;
import iut.tp.strategy.NoTaxStrategy;
import iut.tp.strategy.ReducedTaxStrategy;
import iut.tp.strategy.StandardTaxStrategy;
import iut.tp.strategy.TaxStrategy;
import iut.tp.supply.Supply;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private SuperMarketFacade supermarket;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.supermarket = new SuperMarketFacade();
    }

    public void start() {
        initializeSampleData();

        while (true) {
            displayMainMenu();
            int choice = readInt("Votre choix: ");

            try {
                switch (choice) {
                    case 1: manageProducts(); break;
                    case 2: manageStock(); break;
                    case 3: manageSales(); break;
                    case 4: manageSupplies(); break;
                    case 5: manageCustomers(); break;
                    case 6: displayReports(); break;
                    case 0:
                        System.out.println("\nAu revoir!");
                        return;
                    default:
                        System.out.println("Choix invalide!");
                }
            } catch (Exception e) {
                System.out.println("\nErreur: " + e.getMessage());
            }

            pause();
        }
    }

    private void displayMainMenu() {
        clearScreen();
        System.out.println("=====================================================");
        System.out.println("      SYSTeME DE GESTION DE SUPERMARCHE              ");
        System.out.println("=====================================================");
        System.out.println("\n MENU PRINCIPAL\n");
        System.out.println("1. Gestion des Produits");
        System.out.println("2. Gestion du Stock");
        System.out.println("3. Gestion des Ventes");
        System.out.println("4. Gestion des Approvisionnements");
        System.out.println("5. Gestion des Clients");
        System.out.println("6. Rapports et Consultations");
        System.out.println("0. Quitter");
        System.out.println("\n" + "─".repeat(52));
    }

    private void manageProducts() throws ProductNotFoundException {
        clearScreen();
        System.out.println(" GESTION DES PRODUITS\n");
        System.out.println("1. Creer un produit");
        System.out.println("2. Lister tous les produits");
        System.out.println("3. Modifier un produit");
        System.out.println("4. Supprimer un produit");
        System.out.println("0. Retour");

        int choice = readInt("\nVotre choix: ");

        switch (choice) {
            case 1: createProduct(); break;
            case 2: listProducts(); break;
            case 3: updateProduct(); break;
            case 4: deleteProduct(); break;
        }
    }

    private void createProduct() {
        System.out.println("\nCRAATION D'UN PRODUIT\n");

        String id = readString("ID du produit: ");
        String name = readString("Nom: ");
        String category = readString("Categorie: ");
        double price = readDouble("Prix (FCFA): ");
        String barcode = readString("Code-barres: ");

        supermarket.getProductService().createProduct(id, name, category, price, barcode);

        int initialStock = readInt("Stock initial: ");
        int minThreshold = readInt("Seuil minimum: ");

        try {
            Product product = supermarket.getProductService().getProduct(id);
            supermarket.getStockService().addStock(product, initialStock, minThreshold);
            System.out.println("\nProduit cree avec succes");
        } catch (ProductNotFoundException e) {
            System.out.println("\nErreur lors de la creation");
        }
    }

    private void listProducts() {
        System.out.println("\nLISTE DES PRODUITS\n");
        List<Product> products = supermarket.getProductService().getAllProducts();

        if (products.isEmpty()) {
            System.out.println("Aucun produit enregistre.");
            return;
        }

        System.out.println(String.format("%-10s %-25s %-15s %-12s %-15s",
                "ID", "Nom", "Catégorie", "Prix", "Code-barres"));
        System.out.println("─".repeat(80));

        for (Product p : products) {
            System.out.println(String.format("%-10s %-25s %-15s %,10.2f F %-15s",
                    p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getBarcode()));
        }
    }

    private void updateProduct() throws ProductNotFoundException {
        System.out.println("\n️  MODIFICATION D'UN PRODUIT\n");
        String id = readString("ID du produit a modifier: ");

        Product product = supermarket.getProductService().getProduct(id);

        System.out.println("\nProduit actuel: " + product.getName());
        String name = readString("Nouveau nom: ");
        if (!name.isEmpty()) product.setName(name);

        String category = readString("Nouvelle categorie: ");
        if (!category.isEmpty()) product.setCategory(category);

        String priceStr = readString("Nouveau prix: ");
        if (!priceStr.isEmpty()) product.setPrice(Double.parseDouble(priceStr));

        supermarket.getProductService().updateProduct(product);
        System.out.println("\nProduit modifie avec succes!");
    }

    private void deleteProduct() {
        System.out.println("\n🗑  SUPPRESSION D'UN PRODUIT\n");
        String id = readString("ID du produit a supprimer: ");

        if (readString("Confirmer la suppression? (oui/non): ").equalsIgnoreCase("oui")) {
            supermarket.getProductService().deleteProduct(id);
            System.out.println("\n Produit supprime!");
        }
    }

    // ==================== GESTION DU STOCK ====================

    private void manageStock() {
        clearScreen();
        System.out.println("GESTION DU STOCK\n");
        System.out.println("1. Voir l'etat du stock");
        System.out.println("2. Voir les produits en rupture de stock");
        System.out.println("3. Ajuster le stock manuellement");
        System.out.println("0. Retour");

        int choice = readInt("\nVotre choix: ");

        switch (choice) {
            case 1: viewStock(); break;
            case 2: viewLowStock(); break;
            case 3: adjustStock(); break;
        }
    }

    private void viewStock() {
        System.out.println("\nETAT DU STOCK\n");
        List<StockItem> items = supermarket.getStockService().getAllStock();

        if (items.isEmpty()) {
            System.out.println("Aucun stock enregistré.");
            return;
        }

        System.out.println(String.format("%-25s %-15s %-10s %-10s %-15s",
                "Produit", "Catégorie", "Quantite", "Seuil Min", "Statut"));
        System.out.println("─".repeat(80));

        for (StockItem item : items) {
            String status = item.isLowStock() ? "️  FAIBLE" : "OK";
            System.out.println(String.format("%-25s %-15s %-10d %-10d %-15s",
                    item.getProduct().getName(),
                    item.getProduct().getCategory(),
                    item.getQuantity(),
                    item.getMinThreshold(),
                    status));
        }
    }

    private void viewLowStock() {
        System.out.println("\n️  PRODUITS EN RUPTURE DE STOCK\n");
        List<StockItem> items = supermarket.getStockService().getLowStockItems();

        if (items.isEmpty()) {
            System.out.println(" Aucun produit en rupture de stock!");
            return;
        }

        System.out.println(String.format("%-25s %-10s %-10s",
                "Produit", "Quantite", "Seuil Min"));
        System.out.println("─".repeat(50));

        for (StockItem item : items) {
            System.out.println(String.format("%-25s %-10d %-10d",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getMinThreshold()));
        }
    }

    private void adjustStock() {
        System.out.println("\n AJUSTEMENT DU STOCK\n");
        String productId = readString("ID du produit: ");
        int adjustment = readInt("Quantite a ajouter (negatif pour retirer): ");

        try {
            if (adjustment > 0) {
                supermarket.getStockService().increaseStock(productId, adjustment);
            } else {
                supermarket.getStockService().decreaseStock(productId, Math.abs(adjustment));
            }
            System.out.println("\n Stock ajuste avec succes!");
        } catch (Exception e) {
            System.out.println("\n Erreur: " + e.getMessage());
        }
    }

    // ==================== GESTION DES VENTES ====================

    private void manageSales() {
        clearScreen();
        System.out.println(" GESTION DES VENTES\n");
        System.out.println("1. Creer une nouvelle vente");
        System.out.println("2. Voir toutes les ventes");
        System.out.println("3. Voir le detail d'une vente");
        System.out.println("0. Retour");

        int choice = readInt("\nVotre choix: ");

        switch (choice) {
            case 1: createSale(); break;
            case 2: viewAllSales(); break;
            case 3: viewSaleDetail(); break;
        }
    }

    private void createSale() {
        System.out.println("\nNOUVELLE VENTE\n");

        // Choisir le type de taxe
        System.out.println("Choisir le type de taxe:");
        System.out.println("1. TVA Standard (19.25%)");
        System.out.println("2. TVA Reduite (5.5%)");
        System.out.println("3. Sans taxe");

        int taxChoice = readInt("Votre choix: ");
        TaxStrategy taxStrategy = switch (taxChoice) {
            case 2 -> new ReducedTaxStrategy();
            case 3 -> new NoTaxStrategy();
            default -> new StandardTaxStrategy();
        };

        supermarket.getSaleService().setTaxStrategy(taxStrategy);
        System.out.println("Taxe sélectionnee: " + taxStrategy.getName());

        String saleId = "V" + System.currentTimeMillis();

        String customerId = readString("\nID du client (Entree pour vente anonyme): ");
        Customer customer = null;
        if (!customerId.isEmpty()) {
            customer = supermarket.getCustomerService().getCustomer(customerId);
            if (customer == null) {
                System.out.println("️  Client non trouve. Vente anonyme.");
            }
        }

        Sale sale = supermarket.getSaleService().createSale(saleId, customer);

        System.out.println("\n Ajout des articles (ID vide pour terminer)");

        while (true) {
            String productId = readString("\nID du produit: ");
            if (productId.isEmpty()) break;

            try {
                Product product = supermarket.getProductService().getProduct(productId);
                int available = supermarket.getStockService().getAvailableQuantity(productId);

                System.out.println("Produit: " + product.getName() + " | Prix: " + product.getPrice() + " FCFA");
                System.out.println("Stock disponible: " + available);

                int quantity = readInt("Quantite: ");

                supermarket.getSaleService().addItemToSale(sale, product, quantity);
                System.out.println(" Article ajoute");

            } catch (Exception e) {
                System.out.println(" Erreur: " + e.getMessage());
            }
        }

        if (sale.getItems().isEmpty()) {
            System.out.println("\n Vente annulee (aucun article)");
            return;
        }

        try {
            supermarket.getSaleService().completeSale(sale);

            System.out.println("\n" + "═".repeat(50));
            System.out.println("            TICKET DE CAISSE ");
            System.out.println("═".repeat(50));
            System.out.println("N° Vente: " + sale.getId());
            System.out.println("Date: " + sale.getSaleDate().format(dateFormatter));
            if (sale.getCustomer() != null) {
                System.out.println("Client: " + sale.getCustomer().getName());
            }
            System.out.println("─".repeat(50));

            for (SaleItem item : sale.getItems()) {
                System.out.println(String.format("%-20s %2d x %,8.2f = %,10.2f F",
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()));
            }

            System.out.println("─".repeat(50));
            System.out.println(String.format("Sous-total:            %,15.2f F", sale.getSubtotal()));
            System.out.println(String.format("Taxe (%s):      %,15.2f F",
                    taxStrategy.getName(), sale.getTaxAmount()));
            System.out.println("═".repeat(50));
            System.out.println(String.format("TOTAL TTC:             %,15.2f F", sale.getTotalAmount()));
            System.out.println("═".repeat(50));
            System.out.println("\n Vente enregistree avec succès");

        } catch (Exception e) {
            System.out.println("\n Erreur lors de la finalisation: " + e.getMessage());
        }
    }

    private void viewAllSales() {
        System.out.println("\n LISTE DES VENTES\n");
        List<Sale> sales = supermarket.getSaleService().getAllSales();

        if (sales.isEmpty()) {
            System.out.println("Aucune vente enregistrée.");
            return;
        }

        System.out.println(String.format("%-15s %-20s %-20s %-10s %s",
                "N° Vente", "Client", "Date", "Articles", "Total TTC"));
        System.out.println("─".repeat(90));

        for (Sale sale : sales) {
            String customerName = sale.getCustomer() != null ?
                    sale.getCustomer().getName() : "Anonyme";
            System.out.println(String.format("%-15s %-20s %-20s %-10d %,12.2f F",
                    sale.getId(),
                    customerName,
                    sale.getSaleDate().format(dateFormatter),
                    sale.getItems().size(),
                    sale.getTotalAmount()));
        }
    }

    private void viewSaleDetail() {
        System.out.println("\n DETAIL D'UNE VENTE\n");
        String saleId = readString("N° de vente: ");

        Sale sale = supermarket.getSaleService().getSaleById(saleId);
        if (sale == null) {
            System.out.println(" Vente non trouvée!");
            return;
        }

        System.out.println("\n" + "═".repeat(60));
        System.out.println("N° Vente: " + sale.getId());
        System.out.println("Date: " + sale.getSaleDate().format(dateFormatter));
        if (sale.getCustomer() != null) {
            System.out.println("Client: " + sale.getCustomer().getName());
        }
        System.out.println("─".repeat(60));
        System.out.println("\nArticles:");

        for (SaleItem item : sale.getItems()) {
            System.out.println(String.format("  %-25s %2d x %,8.2f = %,10.2f F",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getSubtotal()));
        }

        System.out.println("─".repeat(60));
        System.out.println(String.format("Sous-total:                      %,15.2f F", sale.getSubtotal()));
        System.out.println(String.format("Taxe:                            %,15.2f F", sale.getTaxAmount()));
        System.out.println("═".repeat(60));
        System.out.println(String.format("TOTAL TTC:                       %,15.2f F", sale.getTotalAmount()));
        System.out.println("═".repeat(60));
    }

    // ==================== GESTION DES APPROVISIONNEMENTS ====================

    private void manageSupplies() {
        clearScreen();
        System.out.println(" GESTION DES APPROVISIONNEMENTS\n");
        System.out.println("1. Creer un approvisionnement");
        System.out.println("2. Voir tous les approvisionnements");
        System.out.println("0. Retour");

        int choice = readInt("\nVotre choix: ");

        switch (choice) {
            case 1: createSupply(); break;
            case 2: viewAllSupplies(); break;
        }
    }

    private void createSupply() {
        System.out.println("\nNOUVEL APPROVISIONNEMENT\n");

        String supplyId = "S" + System.currentTimeMillis();
        String productId = readString("ID du produit: ");

        try {
            Product product = supermarket.getProductService().getProduct(productId);
            System.out.println("Produit: " + product.getName());

            int quantity = readInt("Quantité: ");
            String supplier = readString("Fournisseur: ");

            supermarket.getSupplyService().createSupply(supplyId, product, quantity, supplier);
            System.out.println("\n Approvisionnement enregistre!");
            System.out.println("Le stock a ete automatiquement mis a jour.");

        } catch (ProductNotFoundException e) {
            System.out.println("\n Produit non trouve");
        }
    }

    private void viewAllSupplies() {
        System.out.println("\nLISTE DES APPROVISIONNEMENTS\n");
        List<Supply> supplies = supermarket.getSupplyService().getAllSupplies();

        if (supplies.isEmpty()) {
            System.out.println("Aucun approvisionnement enregistre");
            return;
        }

        System.out.println(String.format("%-15s %-25s %-20s %-10s %s",
                "N° Appro", "Produit", "Date", "Quantite", "Fournisseur"));
        System.out.println("─".repeat(90));

        for (Supply supply : supplies) {
            System.out.println(String.format("%-15s %-25s %-20s %-10d %s",
                    supply.getId(),
                    supply.getProduct().getName(),
                    supply.getSupplyDate().format(dateFormatter),
                    supply.getQuantity(),
                    supply.getSupplier()));
        }
    }

    // ==================== GESTION DES CLIENTS ====================

    private void manageCustomers() {
        clearScreen();
        System.out.println(" GESTION DES CLIENTS\n");
        System.out.println("1. Creer un client");
        System.out.println("2. Lister tous les clients");
        System.out.println("3. Voir les ventes d'un client");
        System.out.println("0. Retour");

        int choice = readInt("\nVotre choix: ");

        switch (choice) {
            case 1: createCustomer(); break;
            case 2: listCustomers(); break;
            case 3: viewCustomerSales(); break;
        }
    }

    private void createCustomer() {
        System.out.println("\n CREATION D'UN CLIENT\n");

        String id = readString("ID du client: ");
        String name = readString("Nom: ");
        String phone = readString("Telephone: ");
        String email = readString("Email: ");

        supermarket.getCustomerService().createCustomer(id, name, phone, email);
        System.out.println("\n Client cree avec succes");
    }

    private void listCustomers() {
        System.out.println("\n LISTE DES CLIENTS\n");
        List<Customer> customers = supermarket.getCustomerService().getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("Aucun client enregistre");
            return;
        }

        System.out.println(String.format("%-10s %-25s %-15s %s",
                "ID", "Nom", "Telephone", "Email"));
        System.out.println("─".repeat(80));

        for (Customer c : customers) {
            System.out.println(String.format("%-10s %-25s %-15s %s",
                    c.getId(), c.getName(), c.getPhone(), c.getEmail()));
        }
    }

    private void viewCustomerSales() {
        System.out.println("\n VENTES PAR CLIENT\n");
        String customerId = readString("ID du client: ");

        Customer customer = supermarket.getCustomerService().getCustomer(customerId);
        if (customer == null) {
            System.out.println(" Client non trouve");
            return;
        }

        List<Sale> sales = supermarket.getSaleService().getSalesByCustomer(customerId);

        System.out.println("\nClient: " + customer.getName());
        System.out.println("Total de ventes: " + sales.size());

        if (sales.isEmpty()) {
            System.out.println("Aucune vente pour ce client.");
            return;
        }

        System.out.println("\n" + String.format("%-15s %-20s %-10s %s",
                "N° Vente", "Date", "Articles", "Total TTC"));
        System.out.println("─".repeat(70));

        double totalAmount = 0;
        for (Sale sale : sales) {
            System.out.println(String.format("%-15s %-20s %-10d %,12.2f F",
                    sale.getId(),
                    sale.getSaleDate().format(dateFormatter),
                    sale.getItems().size(),
                    sale.getTotalAmount()));
            totalAmount += sale.getTotalAmount();
        }

        System.out.println("─".repeat(70));
        System.out.println(String.format("TOTAL:                                   %,12.2f F", totalAmount));
    }

    // ==================== RAPPORTS ====================

    private void displayReports() {
        clearScreen();
        System.out.println("RAPPORTS ET CONSULTATIONS\n");
        System.out.println("1. Liste des ventes");
        System.out.println("2. Détail du contenu des ventes");
        System.out.println("3. Produits en stock");
        System.out.println("4. Produits en rupture de stock");
        System.out.println("5. Liste des approvisionnements");
        System.out.println("6. Ventes par client");
        System.out.println("7. Liste des clients");
        System.out.println("8. Statistiques globales");
        System.out.println("0. Retour");

        int choice = readInt("\nVotre choix: ");

        switch (choice) {
            case 1: viewAllSales(); break;
            case 2: viewAllSalesDetail(); break;
            case 3: viewStock(); break;
            case 4: viewLowStock(); break;
            case 5: viewAllSupplies(); break;
            case 6: viewCustomerSales(); break;
            case 7: listCustomers(); break;
            case 8: displayStatistics(); break;
        }
    }

    private void viewAllSalesDetail() {
        System.out.println("\n DETAIL DE TOUTES LES VENTES\n");
        List<Sale> sales = supermarket.getSaleService().getAllSales();

        if (sales.isEmpty()) {
            System.out.println("Aucune vente enregistree.");
            return;
        }

        for (Sale sale : sales) {
            System.out.println("\n" + "═".repeat(60));
            System.out.println("N° Vente: " + sale.getId());
            System.out.println("Client: " + (sale.getCustomer() != null ?
                    sale.getCustomer().getName() : "Anonyme"));
            System.out.println("Date: " + sale.getSaleDate().format(dateFormatter));
            System.out.println("─".repeat(60));

            for (SaleItem item : sale.getItems()) {
                System.out.println(String.format("  %-25s %2d x %,8.2f = %,10.2f F",
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()));
            }

            System.out.println("─".repeat(60));
            System.out.println(String.format("TOTAL TTC: %,15.2f F", sale.getTotalAmount()));
        }
    }

    private void displayStatistics() {
        System.out.println("\n STATISTIQUES GLOBALES\n");

        int totalProducts = supermarket.getProductService().getAllProducts().size();
        List<StockItem> stockItems = supermarket.getStockService().getAllStock();
        int totalStock = stockItems.stream().mapToInt(StockItem::getQuantity).sum();
        int lowStockCount = supermarket.getStockService().getLowStockItems().size();

        List<Sale> sales = supermarket.getSaleService().getAllSales();
        int totalSales = sales.size();
        double totalRevenue = sales.stream().mapToDouble(Sale::getTotalAmount).sum();

        int totalCustomers = supermarket.getCustomerService().getAllCustomers().size();
        int totalSupplies = supermarket.getSupplyService().getAllSupplies().size();

        System.out.println("==========================================");
        System.out.println("        TABLEAU DE BORD                ");
        System.out.println("=============================================");
        System.out.println(String.format(" Produits:           %,15d ", totalProducts));
        System.out.println(String.format(" Unites en stock:    %,15d ", totalStock));
        System.out.println(String.format(" Alertes stock:      %,15d", lowStockCount));
        System.out.println("=============================================");
        System.out.println(String.format("Ventes:             %,15d ", totalSales));
        System.out.println(String.format(" Chiffre d'affaires: %,12.2f F ", totalRevenue));
        System.out.println("=============================================");
        System.out.println(String.format(" Clients:            %,15d ", totalCustomers));
        System.out.println(String.format("Approvisionnements: %,15d ", totalSupplies));
        System.out.println("===============================================");
    }

    // ==================== DONNEE EXEMPLE ====================

    private void initializeSampleData() {
        // Produits
        supermarket.getProductService().createProduct("P001", "Pain", "Boulangerie", 110, "1234567890");
        supermarket.getProductService().createProduct("P002", "Lait (Liquide 1L)", "Produits laitiers", 800, "0987654321");
        supermarket.getProductService().createProduct("P003", "Sac de Riz (25kg)", "Granule", 25000, "1111111111");
        supermarket.getProductService().createProduct("P004", "Eau (1.5L)", "Boissons", 300, "2222222222");
        supermarket.getProductService().createProduct("P005", "Savon", "Hygiene", 400, "3333333333");

        // Stock
        try {
            supermarket.getStockService().addStock(
                    supermarket.getProductService().getProduct("P001"), 100, 20);
            supermarket.getStockService().addStock(
                    supermarket.getProductService().getProduct("P002"), 50, 15);
            supermarket.getStockService().addStock(
                    supermarket.getProductService().getProduct("P003"), 200, 30);
            supermarket.getStockService().addStock(
                    supermarket.getProductService().getProduct("P004"), 150, 25);
            supermarket.getStockService().addStock(
                    supermarket.getProductService().getProduct("P005"), 10, 20);
        } catch (ProductNotFoundException e) {
            e.printStackTrace();
        }

        // Clients
        supermarket.getCustomerService().createCustomer(
                "C001", "Jean Dupont", "+237 690000001", "jean@email.cm");
        supermarket.getCustomerService().createCustomer(
                "C002", "Marie Nguema", "+237 690000002", "marie@email.cm");
        supermarket.getCustomerService().createCustomer(
                "C003", "Paul Kamga", "+237 690000003", "paul@email.cm");
    }

    // ==================== UTILITAIRES ====================

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void pause() {
        System.out.println("\nAppuyez sur Entree pour continuer");
        scanner.nextLine();
    }
}
