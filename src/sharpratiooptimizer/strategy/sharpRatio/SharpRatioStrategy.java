/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.strategy.sharpRatio;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import sharpratiooptimizer.configuration.ConfigurationHelper;
import sharpratiooptimizer.configuration.EqFileName;
import sharpratiooptimizer.portfolio.Portfolio;
import sharpratiooptimizer.portfolio.PortfolioHelper;
import sharpratiooptimizer.strategy.IStrategy;

/**
 *
 * @author axjyb
 */
public class SharpRatioStrategy implements IStrategy {

    private Comparator comparator;
    public static int RUNS = 1000;
    
    @Inject
    public SharpRatioStrategy(Comparator comparator) {
        this.comparator = comparator;
    }
    
    @Override
    public void execute() {
             
        List<EqFileName> files = ConfigurationHelper.loadConfiguration();
        
        GregorianCalendar gc = new GregorianCalendar(2005, 0, 1);
        GregorianCalendar gc2 = new GregorianCalendar(2012, 10, 12);
        
        PortfolioHelper pfh = new PortfolioHelper();
        
        List<Portfolio> pps = pfh.createSetPortfolio(files, gc, gc2, 10, 60);
        
        for ( int k = 0; k < RUNS; k++) {
            System.out.println(k + " - " +(((float)k/RUNS)*100.0)+"%");
            Collections.sort(pps, comparator);

            for ( int i = 59; i >= 20; i--) {
               pps.remove(i);
            }
            
            int removeCounter = 0;
            List<Portfolio> ppsRemove = new ArrayList<Portfolio>();
            for (Portfolio p : pps) {
                if (
                        p.getProfitMap().get(1).doubleValue()<0 || 
                        p.getProfitMap().get(3).doubleValue()<0 || 
                        p.getProfitMap().get(5).doubleValue()<0
                    ) 
                {
                    ppsRemove.add(p);
                    removeCounter++;
                }
            }
            
            for(Portfolio p : ppsRemove) {
                pps.remove(p);
            }
            
            for ( int i = 1; i < pps.size(); i++) {
                pfh.shiftWeighters(pps.get(i), 3, 100);
            }

            pps.addAll(pfh.createSetPortfolio(files, gc, gc2, 10, 40+removeCounter));
        }
        
        Collections.sort(pps, comparator);
        
         for (Portfolio pp : pps) {
            System.out.println(pp.toString());
        }
         
         System.out.println("-------------------");
         System.out.println(pps.get(0).toString());
    }

    @Override
    public Portfolio getBestPortfolio() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPortfolio(Portfolio p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInitDate(Calendar c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEndDate(Calendar c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRuns(int r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
