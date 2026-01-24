package iut.tp.observer;

import iut.tp.stock.StockItem;

public class StockAlertNotifier implements StockObserver{
    @Override
    public void onLowStock(StockItem stockItem) {
        System.out.println("\n️  ALERTE STOCK FAIBLE ️");
        System.out.println("Produit: " + stockItem.getProduct().getName());
        System.out.println("Quantite restante: " + stockItem.getQuantity());
        System.out.println("Seuil minimum: " + stockItem.getMinThreshold());
    }
}
