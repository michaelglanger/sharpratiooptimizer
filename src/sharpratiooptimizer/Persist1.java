/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer;

import sharpratiooptimizer.dataloader.StockDataLoader;
import sharpratiooptimizer.dataprovider.YahooStockDataProvider;

/**
 *
 * @author axjyb
 */
public class Persist1 {

  public static void main(String[] args) {
      
      StockDataLoader sdl = new StockDataLoader(new YahooStockDataProvider());
      sdl.loadData();
  
  }
    
}
