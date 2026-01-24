package iut.tp.observer;

import iut.tp.stock.StockItem;

public interface StockObserver {
    void onLowStock(StockItem stockItem);
}
