/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataloader;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import sharpratiooptimizer.configuration.ConfigurationHelper;
import sharpratiooptimizer.configuration.MarketDescriptor;
import sharpratiooptimizer.configuration.StockDescriptor;
import sharpratiooptimizer.configuration.Stocks;
import sharpratiooptimizer.data.Market;
import sharpratiooptimizer.data.Stock;
import sharpratiooptimizer.data.Symbol;
import sharpratiooptimizer.dataprovider.StockDataProvider;
import sharpratiooptimizer.equity.ValueData;
import sharpratiooptimizer.equity.ValueDataStock;

/**
 *
 * @author axjyb
 */
public class StockDataLoader {
    
    private String PERSISTENCE_UNIT_NAME = "SharpRatioOptimizerPU";
    private EntityManagerFactory factory;
    private EntityManager em;
    private Stocks stocks;
    
    public StockDataLoader() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }
   
    public void loadData() {
        stocks = ConfigurationHelper.loadStocksConfiguration();
        loadMarkets();
        loadSymbols();
        em.close();
    }
    
    private void loadMarkets() {
        List<MarketDescriptor> markets = stocks.getMarkets();
        
        em.getTransaction().begin();
        for (MarketDescriptor md : markets) {
            Query q = em.createQuery("select m from Market m where m.marketName = :name");
            q.setParameter("name", md.getName());
            List l = q.getResultList();
            if ( l.isEmpty() ) {
                Market market = new Market();
                market.setMarketName(md.getName());
                em.persist(market);
            }
        }
        em.getTransaction().commit();
    }
    
    private String getMarketName(String id) {
        List<MarketDescriptor> markets = stocks.getMarkets();
        
        for (MarketDescriptor md : markets) {
            if (md.getId().equals(id)) {
                return md.getName();
            }
        }
        return id;
    }
    
    private void loadSymbols() {
        List<StockDescriptor> stock = stocks.getStockList();
        
        em.getTransaction().begin();
        for (StockDescriptor st : stock) {
            Query qm = em.createQuery("select m from Market m where m.marketName = :name");
            qm.setParameter("name", getMarketName(st.getMarket()));
            
            Market m = (Market) qm.getSingleResult();
            
            Query q = em.createQuery("select s from Symbol s where s.symbol = :symbol and s.market = :market");
            q.setParameter("symbol", st.getName());
            q.setParameter("market", m);
            List l = q.getResultList();
            if ( l.isEmpty() ) {
                Symbol symbol = new Symbol();
                symbol.setSymbol(st.getName());
                symbol.setMarket(m);
                em.persist(symbol);
            }
        }
        em.getTransaction().commit();
    }
    
    public void loadStocks() {
        Market market = em.find(Market.class, new Long(1));
        Symbol symbol = em.find(Symbol.class, new Long(1));

        StockDataProvider sdp = new StockDataProvider();
        List<ValueData> list = sdp.getData("data/ge2.csv");
        em.getTransaction().begin();
        for(ValueData v : list) {
            ValueDataStock vds = (ValueDataStock) v;
            StringBuilder sb = new StringBuilder();
            sb.append(vds.getDate().toString()).append(" ");
            sb.append(vds.getOpen().toString()).append(" ");
            sb.append(vds.getHigh().toString()).append(" ");
            sb.append(vds.getLow().toString()).append(" ");
            sb.append(vds.getClose().toString()).append(" ");
            sb.append(vds.getVolume()).append(" ");
            sb.append(vds.getAdjclose().toString()).append(" ");
            System.out.println(sb.toString());

            Query q = em.createQuery("select s from Stock s where s.date = :date");
            q.setParameter("date", vds.getDate());
            List l = q.getResultList();
            if ( l.isEmpty() ) {
                Stock stock = new Stock();
                stock.setMktopen(vds.getOpen());
                stock.setHigh(vds.getHigh());
                stock.setLow(vds.getLow());
                stock.setMktclose(vds.getClose());
                stock.setDate(vds.getDate());
                stock.setVolume(vds.getVolume());
                stock.setAdjClose(vds.getAdjclose());
                stock.setSymbol(symbol);
                em.persist(stock);
            }


        }
        em.getTransaction().commit();
    }
}
