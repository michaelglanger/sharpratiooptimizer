/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer;

import java.util.Collections;
import java.util.Comparator;
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
        List<Portfolio> pps = PortfolioHelper.createSetPortfolio(files, 10, 20);
        
        for ( int k = 0; k < 100; k++) {
            Collections.sort(pps, new Comparator<Portfolio>() {

                @Override
                public int compare(Portfolio o1, Portfolio o2) {
                    if (o1.getSharpRatio() < o2.getSharpRatio() && o1.getProfit() < o2.getProfit()) {
                        return 1;
                    } else if (o1.getSharpRatio() > o2.getSharpRatio() && o1.getProfit() > o2.getProfit() ) {
                        return -1;
                    }
                    return 0;
                }

            });

            for ( int i = 19; i >= 8; i--) {
               pps.remove(i);
            }
            
            for ( int i = 1; i < 8; i++) {
                PortfolioHelper.shiftWeighters(pps.get(i), 6, 100);
            }

            pps.addAll(PortfolioHelper.createSetPortfolio(files, 10, 12));
        }
        
         for (Portfolio pp : pps) {
            System.out.println(pp.toString());
        }
         
         System.out.println("-------------------");
         System.out.println(pps.get(0).toString());
        
         
         
    }
        
}
