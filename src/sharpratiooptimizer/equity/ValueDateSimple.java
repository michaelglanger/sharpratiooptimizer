/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.equity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author michaellanger
 */
public class ValueDateSimple implements ValueData {
    
    private double value;
    private int iDate;
    private Date date;

    public ValueDateSimple() {
    }

    public ValueDateSimple(float value, int iDate) {
        this.value = value;
        this.iDate = iDate;
    }

    public ValueDateSimple(float value, int iDate, Date date) {
        this.value = value;
        this.iDate = iDate;
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getiDate() {
        return iDate;
    }

    public void setiDate(int iDate) {
        this.iDate = iDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public BigDecimal getMainData() {
        return new BigDecimal(value);
    }
    
    
    
}
