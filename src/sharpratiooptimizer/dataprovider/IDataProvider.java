/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataprovider;

import java.util.Calendar;
import java.util.List;
import sharpratiooptimizer.equity.ValueData;

/**
 *
 * @author axjyb
 */
public interface IDataProvider {
    
    static final int DATE = 0; 
    static final int OPEN = 1;
    static final int HIGH = 2;
    static final int LOW = 3;       
    static final int CLOSE = 4;
    static final int VOLUME = 5;
    static final int ADJCLOSE = 6;        
    
    static final int DAY = 2; 
    static final int MONTH = 1;
    static final int YEAR = 0;
    
    public List<ValueData> getData(String eqName);
    
    public List<ValueData> getData(String symbol, Calendar initDate, Calendar endDate);
    
}
