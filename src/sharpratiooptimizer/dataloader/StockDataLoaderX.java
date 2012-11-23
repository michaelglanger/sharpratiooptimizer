/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataloader;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import sharpratiooptimizer.configuration.EqFileName;
import sharpratiooptimizer.data.Stock;
import sharpratiooptimizer.data.Symbol;
import sharpratiooptimizer.dataprovider.YahooStockDataProvider;
import sharpratiooptimizer.equity.ValueData;
import sharpratiooptimizer.equity.ValueDataStock;

/**
 *
 * @author axjyb
 */
public class StockDataLoaderX extends StockDataLoader {

    ExecutorService execService;
        

    StockDataLoaderX(YahooStockDataProvider yahooStockDataProvider) {
        super(yahooStockDataProvider);
        execService = Executors.newFixedThreadPool(10);
    }
    
    class XLoader implements Runnable {

        EqFileName fn;

        XLoader(EqFileName f) {
            fn = f;
        }

        @Override
        public void run() {
            
            Query qs = em.createQuery("select s from Symbol s where s.symbol = :symbol");
            qs.setParameter("symbol", getSymbol(fn.getName()));
            Symbol symbol = (Symbol) qs.getSingleResult();

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

            for (ValueData v : list) {
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
                if (l.isEmpty()) {
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
            
        }
    }

    @Override
    protected void loadStocks() {
        em.getTransaction().begin();
        for (EqFileName fn : fileNames) {
            execService.execute(new XLoader(fn));
        }
        em.getTransaction().commit();
    }

    @Override
    protected void closeEntityManager() {
        execService.shutdown();
        
        while (!execService.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(StockDataLoaderX.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        super.closeEntityManager();
    }
    
    
}
