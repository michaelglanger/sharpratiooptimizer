/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Michael G. Langer
 */
public class PortfolioHelper {
    
    private static Map<String, Equity> equitiesCache = new HashMap<String, Equity>();
    
    public static Portfolio createPortofolio(String[] fileNames) {
        return createPortfolio(fileNames, EquityHelper.createWeighters(fileNames.length, 100));
    }
    
    public static Portfolio createPortfolio(String[] fileNames, int[] wgts) {
        Equity[] ll = new Equity[fileNames.length];
        
        for (int i = 0; i < ll.length; i++) {
            if (equitiesCache.containsKey(fileNames[i])) {
                ll[i] = equitiesCache.get(fileNames[i]);
            } else {
                Equity eq = new Equity(fileNames[i]);
                equitiesCache.put(fileNames[i], eq);
                ll[i] = eq;
            }
            ll[i].calculateDailyReturns();
            ll[i].calculateSharpRatio();
        }
        
        return new Portfolio(ll, wgts);
    }
    
    private static boolean contains(int[] arr, int value) {
        for (int i : arr) {
            if ( value == i ) return true;
        }
        return false;
    } 
    
    private static int[] createSetOfIndexes(int initAmount, int amount) {
        int indexes[] = new int[amount];
        for (int i = 0; i < indexes.length; i++) { 
            indexes[i] = -1;
        }
        for (int i = 0; i < indexes.length; i++) {
            int index;
            do {
              index = Math.round((float) ((initAmount-1)*Math.random()));  
            } while (contains(indexes, index));
            indexes[i] = index;
        }
        return indexes;
    }
    
    public static List<Portfolio> createSetPortfolio(String[] files, int amount, int amountPortfolios) {
        List<Portfolio> portfolios = new ArrayList<Portfolio>();
        
        for (int j = 0; j < amountPortfolios; j++) {
            int indexes[] = createSetOfIndexes(files.length, amount);

            List<String> ffs = new ArrayList<String>();
            for ( int i : indexes ) {
                ffs.add(files[i]);
            }
            Portfolio p = createPortofolio(ffs.toArray(new String[0]));
            portfolios.add(p);
        }
        
        return portfolios;
    }
    
    public static void shiftWeighters(Portfolio p, int delta, int max) {
        int[] wws = p.getWeighters();
        p.setWeighters(EquityHelper.shiftWeighters(wws, delta, max));
        p.calculateSharpRatio();
    }
    
}
