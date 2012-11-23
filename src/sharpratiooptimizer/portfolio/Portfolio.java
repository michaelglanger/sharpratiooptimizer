/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.portfolio;

import java.util.HashMap;
import java.util.Map;
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
    private double maxDrowBack;
    
    public Portfolio(Equity[] eqs, int[] wts) {
        ll = eqs;
        weighters = wts;
        calculateSharpRatio();
        calculateProfit();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-- Portfolio --");
        for (int i = 0; i < ll.length; i++) {
            sb.append("\n").append(ll[i].getName()).append(" - ").append(weighters[i]).append(" Sharp ratio: ").append(ll[i].getSharpRatio());;
        }
        sb.append("\nSharp Ratio: ").append(sharpRatio);
        sb.append("\nProfit : ").append(profit);
        sb.append("\nProfit 1 : ").append(profitMap.get(new Integer(1)));
        sb.append("\nProfit 3 : ").append(profitMap.get(new Integer(3))/3);
        sb.append("\nProfit 5 : ").append(profitMap.get(new Integer(5))/5);
        sb.append("\nMax drowback : ").append(maxDrowBack);
        return sb.toString();
    }
    
    public double calculateSharpRatio() {
        sharpRatio = new PortfolioHelper().getCompoundSharpRatio(this);
        return sharpRatio;
    }
    
    public double calculateProfit() {
        PortfolioHelper pfh = new PortfolioHelper();
        profit = pfh.calculatePortfolioProfit(this);
        profitMap.put(new Integer(1), pfh.calculatePortfolioProfit(this, 1));
        profitMap.put(new Integer(3), pfh.calculatePortfolioProfit(this, 3));
        profitMap.put(new Integer(5), pfh.calculatePortfolioProfit(this, 5));
        return profit;
    }
    
    public double calculateMaxDrowBack() {
        
        return maxDrowBack;
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

    public double getMaxDrowBack() {
        return maxDrowBack;
    }

    public void setMaxDrowBack(double maxDrowBack) {
        this.maxDrowBack = maxDrowBack;
    }
    
    public Portfolio clone() {
        Equity[] eqs = new Equity[this.ll.length];
        
        for (int i = 0; i < ll.length; i++) {
            eqs[i] = ll[i];
        }
        
        int[] wts = new int[this.weighters.length];
        
        for (int i = 0; i < weighters.length; i++) {
            wts[i] = weighters[i];
        }
        
        Portfolio p = new Portfolio(eqs, wts);
        return p;
    }
    
}
