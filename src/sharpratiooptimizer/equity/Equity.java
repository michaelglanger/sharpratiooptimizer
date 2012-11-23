/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.equity;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Michael G. Langer
 */
public class Equity {
    private String eqName;
    private List<ValueData> list;
    private List<Double> dailyReturn;
    private Map<Integer, Double> dReturns;
    private double sharpRatio;
    
    public Equity(List<ValueData> list, String name) {
        eqName = name;
        this.list = list;
    }
    
    public String getName() {
        return eqName;
    }
    
    public void calculateDailyReturns() {
        EquityHelper eh = new EquityHelper();
        dailyReturn = eh.getDailyReturn(list);
        dReturns = eh.calculateDailyReturn(list);
    }
    
    public double calculateSharpRatio() {
        sharpRatio = new EquityHelper().getSharpRatio(dailyReturn);
        return sharpRatio;
    }

    public double getSharpRatio() {
        return sharpRatio;
    }
    
    public List<ValueData> getList() {
        return list;
    }

    public void setList(List<ValueData> list) {
        this.list = list;
    }

    public List<Double> getDailyReturn() {
        return dailyReturn;
    }

    public void setDailyReturn(List<Double> dailyReturn) {
        this.dailyReturn = dailyReturn;
    }

    public Map<Integer, Double> getdReturns() {
        return dReturns;
    }

    public void setdReturns(Map<Integer, Double> dReturns) {
        this.dReturns = dReturns;
    }
    
    
    
}
