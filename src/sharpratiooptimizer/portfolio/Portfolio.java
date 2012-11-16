/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.portfolio;

import java.util.HashMap;
import java.util.Map;
import sharpratiooptimizer.equity.EquityHelper;
import sharpratiooptimizer.equity.Equity;

/**
 *
 * @author Michael G. Langer
 */
public class Portfolio {
    
    private Equity[] ll;
    private int[] weighters;
    private double sharpRatio;
    private double profit;
    private Map<Integer, Double> profitMap = new HashMap<Integer, Double>();
    
    public Portfolio(Equity[] eqs, int[] wts) {
        ll = eqs;
        weighters = wts;
        calculateSharpRatio();
        calculateProfit();
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-- Portfolio --");
        for (int i = 0; i < ll.length; i++) {
            sb.append("\n"+ll[i].getName()+ " - " + weighters[i] + " Sharp ratio: " + ll[i].getSharpRatio());;
        }
        sb.append("\nSharp Ratio: "+sharpRatio);
        sb.append("\nProfit : "+ profit);
        sb.append("\nProfit 1 : "+ profitMap.get(new Integer(1)));
        sb.append("\nProfit 3 : "+ profitMap.get(new Integer(3)));
        sb.append("\nProfit 5 : "+ profitMap.get(new Integer(5)));
        return sb.toString();
    }
    
    public double calculateSharpRatio() {
        sharpRatio = EquityHelper.getCompoundSharpRatio(ll, weighters);
        return sharpRatio;
    }
    
    public double calculateProfit() {
        profit = PortfolioHelper.calculatePortfolioProfit(this);
        profitMap.put(new Integer(1), PortfolioHelper.calculatePortfolioProfit(this, 1));
        profitMap.put(new Integer(3), PortfolioHelper.calculatePortfolioProfit(this, 3));
        profitMap.put(new Integer(5), PortfolioHelper.calculatePortfolioProfit(this, 5));
        return profit;
    }
    
    public Equity[] getEquities() {
        return ll;
    }
    
    public double getProfit() {
        return profit;
    }
    
    public double getSharpRatio() {
        return sharpRatio;
    }
    
    public int[] getWeighters() {
        return weighters;
    }
    
    public void setWeighters(int[] ws) {
        weighters = ws;
    }

    public Map<Integer, Double> getProfitMap() {
        return profitMap;
    }

    public void setProfitMap(Map<Integer, Double> profitMap) {
        this.profitMap = profitMap;
    }
    
}
