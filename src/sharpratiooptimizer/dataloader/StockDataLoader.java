/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataloader;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import sharpratiooptimizer.configuration.ConfigurationHelper;
import sharpratiooptimizer.configuration.EqFileName;
import sharpratiooptimizer.configuration.MarketDescriptor;
import sharpratiooptimizer.configuration.StockDescriptor;
import sharpratiooptimizer.configuration.Stocks;
import sharpratiooptimizer.data.Market;
import sharpratiooptimizer.data.Stock;
import sharpratiooptimizer.data.Symbol;
import sharpratiooptimizer.dataprovider.IDataProvider;
import sharpratiooptimizer.dataprovider.StockDataProvider;
import sharpratiooptimizer.equity.ValueData;
import sharpratiooptimizer.equity.ValueDataStock;

/**
 *
 * @author Michael G. Langer
 */
public class StockDataLoader {
    
    private String PERSISTENCE_UNIT_NAME = "SharpRatioOptimizerPU";
    private EntityManagerFactory factory;
    private EntityManager em;
    private Stocks stocks;
    private List<EqFileName> fileNames;
    private IDataProvider sdp;
    
    public StockDataLoader() {
        this(new StockDataProvider());
    }
   
    public StockDataLoader(IDataProvider dataProvider) {
        sdp = dataProvider;
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }
    
    public void loadData() {
        try {
            stocks = ConfigurationHelper.loadStocksConfiguration();
            loadMarkets();
            loadSymbols();

            fileNames = ConfigurationHelper.loadConfiguration();

            loadStocks();
        } finally {
            em.close();
        }
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
    
    private String getSymbol(String id) {
        for (StockDescriptor sd : stocks.getStockList()) {
            if (sd.getId().equals(id)) {
                return sd.getName();
            }
        }
        return id;
    }
    
    private void loadStocks() {
        
        for (EqFileName fn : fileNames) {
        em.getTransaction().begin();    
            Query qs = em.createQuery("select s from Symbol s where s.symbol = :symbol");
            qs.setParameter("symbol", getSymbol(fn.getName()));
            Symbol symbol = (Symbol) qs.getSingleResult();
            
//            List<ValueData> list = sdp.getData(fn.getFileName());
            
            String query = "select s from Stock s where s.symbol = :symbol and s.date = (select max(ss.date) from Stock ss where ss.symbol = :ssymbol)";
            Query q1 = em.createQuery(query);
            q1.setParameter("symbol", symbol);
            q1.setParameter("ssymbol", symbol);
            Calendar date;
            try {
                Stock stk = (Stock) q1.getSingleResult();
                date = new GregorianCalendar();
                date.setTime(stk.getDate());
            } catch (NoResultException e) {
                date = new GregorianCalendar(2000, 0, 1);
            }
                        
            List<ValueData> list = sdp.getData(symbol.getSymbol(), date, new GregorianCalendar());

            for(ValueData v : list) {
                ValueDataStock vds = (ValueDataStock) v;
                StringBuilder sb = new StringBuilder();
                sb.append(symbol.getSymbol()).append(" ");
                sb.append(vds.getDate().toString()).append(" ");
                sb.append(vds.getOpen().toString()).append(" ");
                sb.append(vds.getHigh().toString()).append(" ");
                sb.append(vds.getLow().toString()).append(" ");
                sb.append(vds.getClose().toString()).append(" ");
                sb.append(vds.getVolume()).append(" ");
                sb.append(vds.getAdjclose().toString()).append(" ");
                System.out.println(sb.toString());

                Query q = em.createQuery("select s from Stock s where s.date = :date and s.symbol = :symbol");
                q.setParameter("date", vds.getDate());
                q.setParameter("symbol", symbol);
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
}
