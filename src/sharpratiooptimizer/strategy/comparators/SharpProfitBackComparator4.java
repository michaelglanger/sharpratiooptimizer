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
public class SharpProfitBackComparator4 implements Comparator<Portfolio> {
    
    private static double maxProfit1 = 0;
    private static double maxProfit3 = 0;
    private static double maxProfit5 = 0;
    
    private static int wProfit1 = 30;
    private static int wProfit3 = 30;
    private static int wProfit5 = 30;
    private static int wMaxBackward = 10;
    
    private static int minProfit1 = 8;
    private static int minProfit3 = 8;
    private static int minProfit5 = 8;    
    
    @Override
    public int compare(Portfolio o1, Portfolio o2) {
        
        if (up(o1, o2) 
                &&
               o2.getProfitMap().get(1) >= minProfit1 &&
               o2.getProfitMap().get(3) >= minProfit3 &&
               o2.getProfitMap().get(5) >= minProfit5
                ) {
            return 1;
        } 
//        else if ( up(o2, o1) &&
//               o2.getProfitMap().get(1) <= minProfit1 &&
//               o2.getProfitMap().get(3) <= minProfit3 &&
//               o2.getProfitMap().get(5) <= minProfit5) {
//            return -1;
//        }
        return 0;
    }
    
    
    private boolean up(Portfolio o1, Portfolio o2) {
        double result = calculateResult(o1, o2);
        return o1.getProfit() < o2.getProfit() && 
               o1.getSharpRatio() < o2.getSharpRatio() &&
               o2.getMaxDrowBack() < wMaxBackward && 
               result >= 0.5;
    }
        
    private double calculateResult(Portfolio o1, Portfolio o2) {
        
        if (o1.getProfitMap().get(1) > maxProfit1) {
            maxProfit1 = o1.getProfitMap().get(1);
        }
        if (o2.getProfitMap().get(1) > maxProfit1) {
            maxProfit1 = o2.getProfitMap().get(1);
        }
        if (o1.getProfitMap().get(3) > maxProfit3) {
            maxProfit3 = o1.getProfitMap().get(3);
        }
        if (o2.getProfitMap().get(3) > maxProfit3) {
            maxProfit3 = o2.getProfitMap().get(3);
        }
        if (o1.getProfitMap().get(5) > maxProfit5) {
            maxProfit5 = o1.getProfitMap().get(5);
        }
        if (o2.getProfitMap().get(5) > maxProfit5) {
            maxProfit5 = o2.getProfitMap().get(5);
        }
                
        double o1s1 = 1 / (1 + Math.exp((maxProfit1 / o1.getProfitMap().get(1))));
        double o2s1 = 1 / (1 + Math.exp((maxProfit1 / o2.getProfitMap().get(1))));
                
        double o1s3 = 1 / (1 + Math.exp((maxProfit3 / o1.getProfitMap().get(3))));
        double o2s3 = 1 / (1 + Math.exp((maxProfit3 / o2.getProfitMap().get(3))));
        
        double o1s5 = 1 / (1 + Math.exp((maxProfit3 / o1.getProfitMap().get(5))));
        double o2s5 = 1 / (1 + Math.exp((maxProfit3 / o2.getProfitMap().get(5))));
        
        int iValue = 
                (o1s1 < o2s1 ? wProfit1 : 0) +
                (o1s3 < o2s3 ? wProfit3 : 0) +
                (o1s5 < o2s5 ? wProfit5 : 0);
        
        return (double) iValue / this.totat();
    }
    
   
    
    private double totat() {
        return (double) wProfit1 + wProfit3 + wProfit5;
    }
    
}
