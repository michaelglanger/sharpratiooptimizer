/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.strategy;

import java.util.Calendar;
import sharpratiooptimizer.portfolio.Portfolio;

/**
 *
 * @author axjyb
 */
public interface IStrategy {
    
    void execute();
    
    void setPortfolio(Portfolio p);
    
    Portfolio getBestPortfolio();
    
    void setInitDate(Calendar c);
    
    void setEndDate(Calendar c);
    
    void setRuns(int r);
    
}
