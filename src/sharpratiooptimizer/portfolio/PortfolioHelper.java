/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.portfolio;

import sharpratiooptimizer.equity.EquityHelper;
import sharpratiooptimizer.equity.Equity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sharpratiooptimizer.configuration.EqFileName;

/**
 *
 * @author Michael G. Langer
 */
public class PortfolioHelper {
    
    private static Map<String, Equity> equitiesCache = new HashMap<String, Equity>();
    
    public static Portfolio createPortofolio(List<EqFileName> fileNames) {
        return createPortfolio(fileNames, EquityHelper.createWeighters(fileNames.size(), 100));
    }
    
    public static Portfolio createPortfolio(List<EqFileName> fileNames, int[] wgts) {
        Equity[] ll = new Equity[fileNames.size()];
        
        for (int i = 0; i < ll.length; i++) {
            if (equitiesCache.containsKey(fileNames.get(i).getName())) {
                ll[i] = equitiesCache.get(fileNames.get(i).getName());
            } else {
                Equity eq = new Equity(fileNames.get(i).getFile(), fileNames.get(i).getName());
                equitiesCache.put(fileNames.get(i).getName(), eq);
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
    
    public static List<Portfolio> createSetPortfolio(List<EqFileName> files, int amount, int amountPortfolios) {
        List<Portfolio> portfolios = new ArrayList<Portfolio>();
        
        for (int j = 0; j < amountPortfolios; j++) {
            int indexes[] = createSetOfIndexes(files.size(), amount);

            List<EqFileName> ffs = new ArrayList<EqFileName>();
            for ( int i : indexes ) {
                ffs.add(files.get(i));
            }
            Portfolio p = createPortofolio(ffs);
            portfolios.add(p);
        }
        
        return portfolios;
    }
    
    public static void shiftWeighters(Portfolio p, int delta, int max) {
        int[] wws = p.getWeighters();
        p.setWeighters(EquityHelper.shiftWeighters(wws, delta, max));
        p.calculateSharpRatio();
    }
    
    public static double calculatePortfolioProfit(Portfolio p) {
        double result = 0;
        Equity[] eqs = p.getEquities();
        double pInitValue = 100000;
        double pEndValue = 0;
        for (int i = 0; i < eqs.length; i++) {
            double init = eqs[i].getList().get(0).getValue();
            double shares = pInitValue * p.getWeighters()[i] / init; 
            double end = (eqs[i].getList().get(eqs[i].getList().size()-1).getValue() - init) * shares;
            pEndValue += end;
        }
        result = (pEndValue / pInitValue) - 1;
        return result;
    }
    
}
