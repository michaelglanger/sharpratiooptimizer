/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.equity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author axjyb
 */
public class ValueDataStock implements ValueData {

    static final int DATE = 0; 
    static final int OPEN = 1;
    static final int HIGH = 2;
    static final int LOW = 3;       
    static final int CLOSE = 4;
    static final int VOLUME = 5;
    static final int ADJCLOSE = 6;  
    
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private long volume;
    private BigDecimal adjclose;
    private int iDate;
    private Date date;

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public BigDecimal getAdjclose() {
        return adjclose;
    }

    public void setAdjclose(BigDecimal adjclose) {
        this.adjclose = adjclose;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        iDate = gc.get(Calendar.YEAR)*10000 + gc.get(Calendar.MONTH)*100 + gc.get(Calendar.DAY_OF_MONTH);
    }
    
    public void setDate(int year, int month, int day) {
        date = new GregorianCalendar(year, month-1, day).getTime();
    }
        
    @Override
    public int getiDate() {
        return iDate;
    }

    public void setiDate(int iDate) {
        this.iDate = iDate;
    }

    @Override
    public BigDecimal getMainData() {
        return adjclose;
    }
    
    
    
}
