/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.portfolio;

import sharpratiooptimizer.equity.Equity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static PortfolioHelper instance;
    private static Map<String, Equity> equitiesCache = new HashMap<String, Equity>();
    
    private static ExecutorService execService = Executors.newFixedThreadPool(40); 
    
    static final Logger log = Logger.getLogger(PortfolioHelper.class.getName());

    public static void resetCache() {
        equitiesCache = new HashMap<String, Equity>();
    }
    
    public Portfolio createPortofolio(List<EqFileName> fileNames) {
        return createPortfolio(fileNames, new EquityHelper().createWeighters(fileNames.size(), 100));
    }

    public Portfolio createPortofolio(List<EqFileName> fileNames, Calendar initDate, Calendar endDate) {
        return createPortfolio(fileNames, initDate, endDate, new EquityHelper().createWeighters(fileNames.size(), 100));
    }

    public static void waitAndShutdown() {
        execService.shutdown();
        try {
            execService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(PortfolioHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        execService = Executors.newFixedThreadPool(40); 
    }
    
     public Portfolio createPortofolio(Equity[] equities, int[] wgts, Calendar initDate, Calendar endDate) {
         IDataProvider dataProvider = new DatabaseStockDataProvider();
         for (Equity e : equities) {
             List<ValueData> data = dataProvider.getData(e.getName(), initDate, endDate);
             e.setList(data);
             e.calculateDailyReturns();
             e.calculateSharpRatio();
             equitiesCache.put(e.getName(), e);
         }
         
         return new Portfolio(equities, wgts);
     }
    
    public Portfolio createPortfolio(List<EqFileName> fileNames, Calendar initDate, Calendar endDate, int[] wgts) {
        Equity[] ll = new Equity[fileNames.size()];
        IDataProvider dataProvider = new DatabaseStockDataProvider();
        Future[] ff = new Future[ll.length];
        for (int i = 0; i < ll.length; i++) {
            Future f = null;
            if (equitiesCache.containsKey(fileNames.get(i).getSymbol())) {
                ll[i] = equitiesCache.get(fileNames.get(i).getSymbol());
            } else {
                List<ValueData> data = dataProvider.getData(fileNames.get(i).getSymbol(), initDate, endDate);
                Equity eq = new Equity(data, fileNames.get(i).getSymbol());
                Calculator calculator = new Calculator(eq);
                ff[i] = execService.submit(calculator);

                equitiesCache.put(fileNames.get(i).getSymbol(), eq);
                ll[i] = eq;
            }
        }
        for (Future f : ff) {
            if (f!=null) {
                try {
                    f.get();
                } catch (InterruptedException ex) {
                    Logger.getLogger(PortfolioHelper.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(PortfolioHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return new Portfolio(ll, wgts);
    }
    
    class Calculator implements Runnable {

        Equity e;

        public Calculator(Equity equity) {
            e = equity;
        }
        
        @Override
        public void run() {
            e.calculateDailyReturns();
            e.calculateSharpRatio();
        }
        
    }


    @Deprecated
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

    private int[] createSetOfIndexes(int initAmount, int amount) {
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
    
    public List<Portfolio> createSetPortfolio(List<EqFileName> files, Calendar initDate, Calendar endDate, int amountMin, int amountMax, int amountPortfolios) {
        List<Portfolio> portfolios = new ArrayList<Portfolio>();

        int delta = amountMax - amountMin;
        
        for (int j = 0; j < amountPortfolios; j++) {
            int amount = (int) Math.round(delta * Math.random()) + amountMin;
            
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

    public List<Portfolio> createSetPortfolio(List<EqFileName> files, Calendar initDate, Calendar endDate, int amount, int amountPortfolios) {
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

    public List<Portfolio> createSetPortfolio(List<EqFileName> files, int amount, int amountPortfolios) {
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

    public void shiftWeighters(Portfolio p, int delta, int max) {
        int[] wws = p.getWeighters();
        p.setWeighters(new EquityHelper().shiftWeighters(wws, delta, max));
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

    public double calculatePortfolioProfit(Portfolio p, int time) {
        double result;

        Equity[] eqs = p.getEquities();
        double pInitValue = 100000;
        double pEndValue = 0;
        for (int i = 0; i < eqs.length; i++) {
            List<ValueData> l = eqs[i].getList();
            GregorianCalendar gc = new GregorianCalendar();

            int date = (gc.get(Calendar.YEAR) - time) * 10000
                    + gc.get(Calendar.MONTH) * 100
                    + gc.get(Calendar.DAY_OF_MONTH);



            int index = getIndex(l, date);

            double init = eqs[i].getList().get(index).getMainData().doubleValue();
            double shares = pInitValue * p.getWeighters()[i] / init;
            double end = (eqs[i].getList().get(eqs[i].getList().size() - 1).getMainData().doubleValue() - init) * shares;
            pEndValue += end;
        }
        result = (pEndValue / pInitValue) - 1;

        return result;
    }

    public double calculatePortfolioProfit(Portfolio p) {
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
    
    public double getCompoundSharpRatio(Portfolio p) {
        
        Equity[] eqs = p.getEquities();
        int[] weights = p.getWeighters();

        List<Integer> datesList = getDatesList(eqs);
        List<Double> dailyReturns = new ArrayList<Double>();
        for (Integer i : datesList) {
            double sum = 0;
            for (int j = 0; j < eqs.length; j++) {
                Map<Integer, Double> dr = eqs[j].getdReturns();
                if (dr == null) {
                    log.info(eqs[j].getName());
                }
                Double d = dr.get(i);
                if (d==null) {
                    d = 0d;
                } else {
                    sum += d * ((float) weights[j]);
                }
            }
            dailyReturns.add(sum);
        }
        
        double init = 10000;
        double ih = init;
        double il = init;
//        double highest = dailyReturns.get(0);
//        double lowest = dailyReturns.get(0);
        for (Double d : dailyReturns) {
            init = init*(1+d);
            if (init > ih) {
                ih = init;
            }
            if (init < il) {
                il = init;
            }
        }
        p.setMaxDrowBack((ih-il)/10000);
        return new EquityHelper().getSharpRatio(dailyReturns);
    }
    
    public List<Integer> getDatesList(Equity[] eqs) {
        Set<Integer> sResult = new HashSet<Integer>();
        
        for (Equity e : eqs) {
            for (ValueData vd : e.getList()) {
                sResult.add(vd.getiDate());
            }
        }
        
        List<Integer> result = new ArrayList<Integer>();
        
        result.addAll(sResult);
        
        Collections.sort(result, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (o1<o2 ? -1 : (o1==o2 ? 0 : 1));
            }
        });
        
        return result;
    }
    
//    public double calculatePortfolioDrowBack(Portfolio p) {
//        double result = 0;
//        
//        Equity[] eqs = p.getEquities();
//        for (int i = 0; i < eqs.length; i++) {
//            for ()
//        }
//        
//        return result;
//    }
}
