/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataloader;

import sharpratiooptimizer.dataprovider.YahooStockDataProvider;

/**
 *
 * @author axjyb
 */
public class RunStockDataLoader {

  public static void main(String[] args) {
      
      StockDataLoader sdl = new StockDataLoader(new YahooStockDataProvider());
      sdl.loadData();
  
  }
    
}
