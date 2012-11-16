/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import sharpratiooptimizer.configuration.ConfigurationHelper;
import sharpratiooptimizer.configuration.EqFileName;
import sharpratiooptimizer.portfolio.Portfolio;
import sharpratiooptimizer.portfolio.PortfolioHelper;

/**
 *
 * @author michaellanger
 */
public class SharpRatioOptimizer {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<EqFileName> files = ConfigurationHelper.loadConfiguration();
        
        GregorianCalendar gc = new GregorianCalendar(2002, 0, 1);
        GregorianCalendar gc2 = new GregorianCalendar(2012, 10, 12);
        
        List<Portfolio> pps = PortfolioHelper.createSetPortfolio(files, gc, gc2, 10, 60);
        
        for ( int k = 0; k < 50; k++) {
            Collections.sort(pps, new Comparator<Portfolio>() {

                @Override
                public int compare(Portfolio o1, Portfolio o2) {
                    if (
                            o1.getSharpRatio() < o2.getSharpRatio() && 
                            o1.getProfit() < o2.getProfit() &&
                            o1.getProfitMap().get(1) < o2.getProfitMap().get(1) &&
                            o1.getProfitMap().get(3) < o2.getProfitMap().get(3) &&
                            o1.getProfitMap().get(5) < o2.getProfitMap().get(5)
                        ) 
                    {
                        return 1;
                    } else if (
                            o1.getSharpRatio() > o2.getSharpRatio() && 
                            o1.getProfit() > o2.getProfit() &&
                            o1.getProfitMap().get(1) > o2.getProfitMap().get(1) &&
                            o1.getProfitMap().get(3) > o2.getProfitMap().get(3) &&
                            o1.getProfitMap().get(5) > o2.getProfitMap().get(5)
                              ) 
                    {
                        return -1;
                    }
                    return 0;
                }

            });

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
                PortfolioHelper.shiftWeighters(pps.get(i), 3, 100);
            }

            pps.addAll(PortfolioHelper.createSetPortfolio(files, gc, gc2, 10, 40+removeCounter));
        }
        
        Collections.sort(pps, new Comparator<Portfolio>() {

                @Override
                public int compare(Portfolio o1, Portfolio o2) {
                    if (
                            o1.getSharpRatio() < o2.getSharpRatio() && 
                            o1.getProfit() < o2.getProfit() &&
                            o1.getProfitMap().get(1) < o2.getProfitMap().get(1) &&
                            o1.getProfitMap().get(3) < o2.getProfitMap().get(3) &&
                            o1.getProfitMap().get(5) < o2.getProfitMap().get(5)
                        ) 
                    {
                        return 1;
                    } else if (
                            o1.getSharpRatio() > o2.getSharpRatio() && 
                            o1.getProfit() > o2.getProfit() &&
                            o1.getProfitMap().get(1) > o2.getProfitMap().get(1) &&
                            o1.getProfitMap().get(3) > o2.getProfitMap().get(3) &&
                            o1.getProfitMap().get(5) > o2.getProfitMap().get(5)
                              ) 
                    {
                        return -1;
                    }
                    return 0;
                }

            });
        
         for (Portfolio pp : pps) {
            System.out.println(pp.toString());
        }
         
         System.out.println("-------------------");
         System.out.println(pps.get(0).toString());
        
         
         
    }
        
}
