/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.portfolio;

import java.math.BigDecimal;
import sharpratiooptimizer.equity.Equity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sharpratiooptimizer.configuration.EqFileName;
import sharpratiooptimizer.dataprovider.DatabaseStockDataProvider;
import sharpratiooptimizer.dataprovider.IDataProvider;
import sharpratiooptimizer.dataprovider.PlainDataProvider;
import sharpratiooptimizer.equity.EquityHelper;
import sharpratiooptimizer.equity.ValueData;

/**
 *
 * @author Michael G. Langer
 */
public class PortfolioHelper {

    private static Map<String, Equity> equitiesCache = new HashMap<String, Equity>();

    public static Portfolio createPortofolio(List<EqFileName> fileNames) {
        return createPortfolio(fileNames, EquityHelper.createWeighters(fileNames.size(), 100));
    }
    
    public static Portfolio createPortofolio(List<EqFileName> fileNames, Calendar initDate, Calendar endDate) {
        return createPortfolio(fileNames, initDate, endDate, EquityHelper.createWeighters(fileNames.size(), 100));
    }

    public static Portfolio createPortfolio(List<EqFileName> fileNames, Calendar initDate, Calendar endDate, int[] wgts) {
        Equity[] ll = new Equity[fileNames.size()];
        IDataProvider dataProvider = new DatabaseStockDataProvider();
        
        for (int i = 0; i < ll.length; i++) {
            if (equitiesCache.containsKey(fileNames.get(i).getName())) {
                ll[i] = equitiesCache.get(fileNames.get(i).getName());
            } else {
                List<ValueData> data = dataProvider.getData(fileNames.get(i).getSymbol(), initDate, endDate);
                Equity eq = new Equity(data, fileNames.get(i).getName());
                equitiesCache.put(fileNames.get(i).getName(), eq);
                ll[i] = eq;
            }
            ll[i].calculateDailyReturns();
            ll[i].calculateSharpRatio();
        }
        
        return new Portfolio(ll, wgts);
    }
    
    public static Portfolio createPortfolio(List<EqFileName> fileNames, int[] wgts) {
        Equity[] ll = new Equity[fileNames.size()];
        IDataProvider dataProvider = new PlainDataProvider();
        for (int i = 0; i < ll.length; i++) {
            if (equitiesCache.containsKey(fileNames.get(i).getName())) {
                ll[i] = equitiesCache.get(fileNames.get(i).getName());
            } else {
                Equity eq = new Equity(dataProvider.getData(fileNames.get(i).getName()), fileNames.get(i).getName());
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
            if (value == i) {
                return true;
            }
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
                index = Math.round((float) ((initAmount - 1) * Math.random()));
            } while (contains(indexes, index));
            indexes[i] = index;
        }
        return indexes;
    }

    public static List<Portfolio> createSetPortfolio(List<EqFileName> files, Calendar initDate, Calendar endDate, int amount, int amountPortfolios) {
        List<Portfolio> portfolios = new ArrayList<Portfolio>();

        for (int j = 0; j < amountPortfolios; j++) {
            int indexes[] = createSetOfIndexes(files.size(), amount);

            List<EqFileName> ffs = new ArrayList<EqFileName>();
            for (int i : indexes) {
                ffs.add(files.get(i));
            }
            Portfolio p = createPortofolio(ffs, initDate, endDate);
            portfolios.add(p);
        }

        return portfolios;
    }

    public static List<Portfolio> createSetPortfolio(List<EqFileName> files, int amount, int amountPortfolios) {
        List<Portfolio> portfolios = new ArrayList<Portfolio>();

        for (int j = 0; j < amountPortfolios; j++) {
            int indexes[] = createSetOfIndexes(files.size(), amount);

            List<EqFileName> ffs = new ArrayList<EqFileName>();
            for (int i : indexes) {
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

    private static int getIndex(List<ValueData> list, int iDate) {
        
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getiDate() == iDate || list.get(i).getiDate() > iDate) {
                return i;
            }
        }
        
        return 0;
    }
    
    public static double calculatePortfolioProfit(Portfolio p, int time) {
        double result;
        
        Equity[] eqs = p.getEquities();
        double pInitValue = 100000;
        double pEndValue = 0;
        for (int i = 0; i < eqs.length; i++) {
            List<ValueData> l = eqs[i].getList();
            GregorianCalendar gc = new GregorianCalendar();
            
            int date = (gc.get(Calendar.YEAR)-time) * 10000 +
                        gc.get(Calendar.MONTH) * 100 +
                        gc.get(Calendar.DAY_OF_MONTH);

            
            
            int index = getIndex(l, date);
            
            double init = eqs[i].getList().get(index).getMainData().doubleValue();
            double shares = pInitValue * p.getWeighters()[i] / init;
            double end = (eqs[i].getList().get(eqs[i].getList().size() - 1).getMainData().doubleValue() - init) * shares;
            pEndValue += end;
        }
        result = (pEndValue / pInitValue) - 1;
        
        return result;
    }
    
    
    public static double calculatePortfolioProfit(Portfolio p) {
        double result = 0;
        Equity[] eqs = p.getEquities();
        double pInitValue = 100000;
        double pEndValue = 0;
        for (int i = 0; i < eqs.length; i++) {
            double init = eqs[i].getList().get(0).getMainData().doubleValue();
            double shares = pInitValue * p.getWeighters()[i] / init;
            double end = (eqs[i].getList().get(eqs[i].getList().size() - 1).getMainData().doubleValue() - init) * shares;
            pEndValue += end;
        }
        result = (pEndValue / pInitValue) - 1;
        return result;
    }
}
