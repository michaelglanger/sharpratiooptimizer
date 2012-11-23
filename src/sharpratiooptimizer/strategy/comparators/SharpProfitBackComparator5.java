package sharpratiooptimizer.strategy.comparators;

import java.util.Comparator;

import sharpratiooptimizer.portfolio.Portfolio;

public class SharpProfitBackComparator5 implements Comparator<Portfolio> {

	private static int minProfit1 = 10;
    private static int minProfit3 = 10;
    private static int minProfit5 = 10; 
	
    @Override
    public int compare(Portfolio o1, Portfolio o2) {
        if (
        		o1.getSharpRatio() < o2.getSharpRatio()
                && o1.getProfit() < o2.getProfit()
                && o1.getProfitMap().get(1) < o2.getProfitMap().get(1)
                && o1.getProfitMap().get(3) < o2.getProfitMap().get(3)
                && o1.getProfitMap().get(5) < o2.getProfitMap().get(5)
                && o1.getMaxDrowBack() > o2.getMaxDrowBack()
                && o2.getProfitMap().get(1) >= minProfit1 
                && o2.getProfitMap().get(3) >= minProfit3 
                && o2.getProfitMap().get(5) >= minProfit5
        ) {
            return 1;
        } else if (
        		o1.getSharpRatio() > o2.getSharpRatio()
                && o1.getProfit() > o2.getProfit()
                && o1.getProfitMap().get(1) > o2.getProfitMap().get(1)
                && o1.getProfitMap().get(3) > o2.getProfitMap().get(3)
                && o1.getProfitMap().get(5) > o2.getProfitMap().get(5)
                && o1.getMaxDrowBack() < o2.getMaxDrowBack()
         ) {
            return -1;
        }
        return 0;
    }
}

