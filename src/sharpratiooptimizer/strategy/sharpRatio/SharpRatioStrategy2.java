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
import java.util.logging.Logger;
import sharpratiooptimizer.configuration.ConfigurationHelper;
import sharpratiooptimizer.configuration.EqFileName;
import sharpratiooptimizer.portfolio.Portfolio;
import sharpratiooptimizer.portfolio.PortfolioHelper;
import sharpratiooptimizer.strategy.IStrategy;

/**
 *
 * @author axjyb
 */
public class SharpRatioStrategy2 implements IStrategy {

    private Comparator comparator;
    public static int RUNS = 50;
    private Portfolio portfolio;
    private Portfolio initPortfolio;
    private Calendar initDate = new GregorianCalendar(2005, 0, 1);
    private Calendar endDate = new GregorianCalendar(2012, 10, 12);
    
    static final Logger log = Logger.getLogger(SharpRatioStrategy2.class.getName());
    
    @Inject
    public SharpRatioStrategy2(Comparator comparator) {
        this.comparator = comparator;
    }
    
    @Override
    public void execute() {
        
        List<EqFileName> files = ConfigurationHelper.loadConfiguration();
        
        PortfolioHelper pfh = new PortfolioHelper();
        
        List<Portfolio> pps;
        if (initPortfolio != null) { 
            pps = pfh.createSetPortfolio(files, initDate, endDate, 4, 15, 29);
            pps.add(initPortfolio);
        } else {
            pps = pfh.createSetPortfolio(files, initDate, endDate, 4, 15, 30);
        }
        
        for ( int k = 0; k < RUNS; k++) {
            log.info(k + " - " +(((float)k/RUNS)*100.0)+"%");
            Collections.sort(pps, comparator);
           
            log.info(pps.get(0).toString());
            log.info("-------------------------------------------------------------------------------");

            for ( int i = pps.size()-1; i >= 10; i--) {
               pps.remove(i);
            }
                
            List<Portfolio> cloned = new ArrayList<Portfolio>();
            for (Portfolio p : pps) {
                Portfolio clon = p.clone();
                pfh.shiftWeighters(clon, 3, 100);
                cloned.add(clon);
            }
            pps.addAll(cloned);
            
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
            
//            for ( int i = 1; i < pps.size(); i++) {
//                pfh.shiftWeighters(pps.get(i), 3, 100);
//            }

            pps.addAll(pfh.createSetPortfolio(files, initDate, endDate, 4, 15, 10+removeCounter));
        }
        pfh.waitAndShutdown();
        Collections.sort(pps, comparator);
        
         for (Portfolio pp : pps) {
            log.info(pp.toString());
        }
         
         log.info("-------------------");
         log.info(pps.get(0).toString());
         portfolio = pps.get(0);
    }

    @Override
    public Portfolio getBestPortfolio() {
        return portfolio;
    }

    @Override
    public void setPortfolio(Portfolio p) {
        initPortfolio = p;
    }

    @Override
    public void setInitDate(Calendar c) {
        initDate = c;
    }

    @Override
    public void setEndDate(Calendar c) {
        endDate = c;
    }

    @Override
    public void setRuns(int r) {
        RUNS = r;
    }
    
    
}
