/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.strategy.comparators;

import java.util.Comparator;
import sharpratiooptimizer.portfolio.Portfolio;

/**
 *
 * @author axjyb
 */
public class SharpProfitBackComparator2 implements Comparator<Portfolio> {

    private static int wSharp = 100;
    private static int wProfit = 50;
    private static int wProfit1 = 30;
    private static int wProfit3 = 30;
    private static int wProfit5 = 30;
    private static int wMaxBackward = 30;
    
    @Override
    public int compare(Portfolio o1, Portfolio o2) {
        double result = calculateResult(o1, o2);
        if (result >= 0.5 ) {
            return 1;
        } else if (result < 0.5) {
            return -1;
        }
        return 0;
    }
        
    private double calculateResult(Portfolio o1, Portfolio o2) {
        int iValue = 
                (o1.getSharpRatio() < o2.getSharpRatio() ? wSharp : 0) +
                (o1.getProfit() < o2.getProfit() ? wProfit : 0) +
                (o1.getProfitMap().get(1) < o2.getProfitMap().get(1) ? wProfit1 : 0) +
                (o1.getProfitMap().get(3) < o2.getProfitMap().get(3) ? wProfit3 : 0) +
                (o1.getProfitMap().get(5) < o2.getProfitMap().get(5) ? wProfit5 : 0) +
                (o1.getMaxDrowBack() > o2.getMaxDrowBack() ? wMaxBackward : 0);
        
        return (double) iValue / this.totat();
    }
    
   
    
    private double totat() {
        return (double) wSharp + wProfit + wProfit1 + wProfit3 + wProfit5 + wMaxBackward;
    }
   
}
