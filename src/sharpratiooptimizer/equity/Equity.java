/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.equity;

import java.io.File;
import java.util.List;

/**
 *
 * @author Michael G. Langer
 */
public class Equity {
    private String eqName;
    private List<ValueDateSimple> list;
    private List<Double> dailyReturn;
    private double sharpRatio;
        
    public Equity(String fileName) {
        this(fileName, fileName);
    }
    
    public Equity(String fileName, String name) {
        eqName = name;
        list = new EquityHelper().readFromFile(fileName);
    }
    
    public Equity(File file, String name) {
        eqName = name;
        list = new EquityHelper().readFromFile(file);
    }
    
    public String getName() {
        return eqName;
    }
    
    public void calculateDailyReturns() {
        dailyReturn = new EquityHelper().getDailyReturn(list);
    }
    
    public double calculateSharpRatio() {
        sharpRatio = EquityHelper.getSharpRatio(dailyReturn);
        return getSharpRatio();
    }

    public double getSharpRatio() {
        return sharpRatio;
    }
    
    public List<ValueDateSimple> getList() {
        return list;
    }

    public void setList(List<ValueDateSimple> list) {
        this.list = list;
    }

    public List<Double> getDailyReturn() {
        return dailyReturn;
    }

    public void setDailyReturn(List<Double> dailyReturn) {
        this.dailyReturn = dailyReturn;
    }
    
    
    
}
