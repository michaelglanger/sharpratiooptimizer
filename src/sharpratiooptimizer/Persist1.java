/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import sharpratiooptimizer.data.Market;
import sharpratiooptimizer.data.Stock;
import sharpratiooptimizer.data.Symbol;
import sharpratiooptimizer.dataloader.StockDataLoader;
import sharpratiooptimizer.dataprovider.StockDataProvider;
import sharpratiooptimizer.equity.ValueData;
import sharpratiooptimizer.equity.ValueDataStock;

/**
 *
 * @author axjyb
 */
public class Persist1 {
    
   private static final String PERSISTENCE_UNIT_NAME = "SharpRatioOptimizerPU";
  private static EntityManagerFactory factory;

  public static void main(String[] args) {
      
      StockDataLoader sdl = new StockDataLoader();
      sdl.loadData();
      
//    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//    EntityManager em = factory.createEntityManager();
//    
//    
//    Market market = em.find(Market.class, new Long(1));
//    Symbol symbol = em.find(Symbol.class, new Long(1));
//    
//    StockDataProvider sdp = new StockDataProvider();
//    List<ValueData> list = sdp.getData("data/ge2.csv");
//    em.getTransaction().begin();
//    for(ValueData v : list) {
//        ValueDataStock vds = (ValueDataStock) v;
//        StringBuilder sb = new StringBuilder();
//        sb.append(vds.getDate().toString()).append(" ");
//        sb.append(vds.getOpen().toString()).append(" ");
//        sb.append(vds.getHigh().toString()).append(" ");
//        sb.append(vds.getLow().toString()).append(" ");
//        sb.append(vds.getClose().toString()).append(" ");
//        sb.append(vds.getVolume()).append(" ");
//        sb.append(vds.getAdjclose().toString()).append(" ");
//        System.out.println(sb.toString());
//        
//        Query q = em.createQuery("select s from Stock s where s.date = :date");
//        q.setParameter("date", vds.getDate());
//        List l = q.getResultList();
//        if ( l.isEmpty() ) {
//            Stock stock = new Stock();
//            stock.setMktopen(vds.getOpen());
//            stock.setHigh(vds.getHigh());
//            stock.setLow(vds.getLow());
//            stock.setMktclose(vds.getClose());
//            stock.setDate(vds.getDate());
//            stock.setVolume(vds.getVolume());
//            stock.setAdjClose(vds.getAdjclose());
//            stock.setSymbol(symbol);
//            em.persist(stock);
//        }
//        
//        
//    }
//    em.getTransaction().commit();
//    
//    int i = 0;
    
    // Read the existing entries and write to console
//    Query q = em.createQuery("select m from Market m where m.marketName = 'NYSE'");
//    List<Market> marketList = q.getResultList();
//    
//    Market m = (Market) q.getResultList().get(0);
//    
//    for (Market todo : marketList) {
//      System.out.println("Market: " + todo.getMarketName());
//    }
//    System.out.println("Size: " + todoList.size());
//
//    // Create new todo
//    em.getTransaction().begin();
//    Todo todo = new Todo();
//    todo.setSummary("This is a test");
//    todo.setDescription("This is a test");
//    em.persist(todo);
//    em.getTransaction().commit();

//    em.close();
  }
    
}
