/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.crawler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
import sharpratiooptimizer.portfolio.Portfolio;
import sharpratiooptimizer.portfolio.PortfolioHelper;
import sharpratiooptimizer.strategy.IStrategy;

/**
 *
 * @author axjyb
 */
public class Crawler {
    
    private Portfolio portfolio;
    private int dayInMilis = 1000 * 60 *60 * 24;
    
    static final Logger log = Logger.getLogger(Crawler.class.getName());
    
    public void execute() {
        Injector injector = Guice.createInjector(new CrawlerStrategyModule());
        IStrategy strategy = injector.getInstance(IStrategy.class);
        Calendar c1 = new GregorianCalendar(2000, 0, 1);
        Calendar c2 = new GregorianCalendar(2005, 0, 1);
        strategy.setInitDate(c1);
        strategy.setEndDate(c2);
        strategy.execute();
        portfolio = strategy.getBestPortfolio();
        log.info("Best: \n: "+ portfolio.toString());
        
        
        while (c2.getTimeInMillis() < new GregorianCalendar(2007, 0, 1).getTimeInMillis()-dayInMilis) {
            PortfolioHelper.resetCache();
            c2.add(Calendar.DAY_OF_YEAR, 1);
//            c2.setTimeInMillis(c2.getTimeInMillis() + dayInMilis);
            log.info(c2.get(Calendar.DAY_OF_MONTH) + "/" +(c2.get(Calendar.MONTH)+1) + "/" + c2.get(Calendar.YEAR));
            portfolio = new PortfolioHelper().createPortofolio(portfolio.getEquities(), portfolio.getWeighters(), c1, c2);
            strategy.setPortfolio(portfolio);
            strategy.setEndDate(c2);
            strategy.execute();
        }
        
        
        log.info("Best: \n: "+ portfolio.toString());
    }
            
    
}
