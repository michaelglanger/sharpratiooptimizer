/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.portfolio;

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
        return sb.toString();
    }
    
    public double calculateSharpRatio() {
        sharpRatio = EquityHelper.getCompoundSharpRatio(ll, weighters);
        return sharpRatio;
    }
    
    public double calculateProfit() {
        profit = PortfolioHelper.calculatePortfolioProfit(this);
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
    
}
