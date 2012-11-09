/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author axjyb
 */
public class Stocks {
    
    List<MarketDescriptor> markets = new ArrayList<MarketDescriptor>();
    List<StockDescriptor> stockList = new ArrayList<StockDescriptor>();

    public List<MarketDescriptor> getMarkets() {
        return markets;
    }

    public void setMarkets(List<MarketDescriptor> markets) {
        this.markets = markets;
    }

    public List<StockDescriptor> getStockList() {
        return stockList;
    }

    public void setStockList(List<StockDescriptor> stockList) {
        this.stockList = stockList;
    }
    
    
    
}
