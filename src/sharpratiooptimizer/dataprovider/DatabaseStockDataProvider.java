/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataprovider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import sharpratiooptimizer.data.Stock;
import sharpratiooptimizer.data.Symbol;
import sharpratiooptimizer.equity.ValueData;
import sharpratiooptimizer.equity.ValueDataStock;

/**
 *
 * @author axjyb
 */
public class DatabaseStockDataProvider implements IDataProvider {
        
    private String PERSISTENCE_UNIT_NAME = "SharpRatioOptimizerPU";
    
    @Override
    public List<ValueData> getData(String eqName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ValueData> getData(String symbol, Calendar initDate, Calendar endDate) {
        List<ValueData> list = new ArrayList<ValueData>();
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        
        Query qs = em.createQuery("select s from Symbol s where s.symbol = :symbol");
        qs.setParameter("symbol", symbol);
        Symbol smbl = null;
        try {
            smbl = (Symbol) qs.getSingleResult();
        } catch (NoResultException e) {
            System.out.println(symbol);
            e.printStackTrace();
        }
        
        Query q = em.createQuery("select s from Stock s where s.symbol = :symbol and s.date between :initDate and :endDate");
        q.setParameter("symbol", smbl);
        q.setParameter("initDate", initDate.getTime());
        q.setParameter("endDate", endDate.getTime());
        
        for ( Stock s : (List<Stock>)q.getResultList()) {
            ValueDataStock vds = new ValueDataStock();
                    
            vds.setOpen(s.getMktopen());
            vds.setHigh(s.getHigh());
            vds.setLow(s.getLow());
            vds.setClose(s.getMktclose());
            vds.setVolume(s.getVolume());
            vds.setAdjclose(s.getAdjClose());
            vds.setDate(s.getDate());
            list.add(vds);
        }
        
        em.close();
        
        Collections.sort(list, new Comparator<ValueData>() {
                @Override
                public int compare(ValueData t, ValueData t1) {
                    return (t.getiDate()<t1.getiDate() ? -1 : (t.getiDate()==t1.getiDate()? 0 : 1));
                }
            });
        
        return list;
    }
    
}
