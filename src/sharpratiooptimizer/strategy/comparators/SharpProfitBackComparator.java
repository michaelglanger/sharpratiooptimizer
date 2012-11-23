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
public class SharpProfitBackComparator implements Comparator<Portfolio> {

    @Override
    public int compare(Portfolio o1, Portfolio o2) {
        if (o1.getSharpRatio() < o2.getSharpRatio()
                && o1.getProfit() < o2.getProfit()
                && o1.getProfitMap().get(1) < o2.getProfitMap().get(1)
                && o1.getProfitMap().get(3) < o2.getProfitMap().get(3)
                && o1.getProfitMap().get(5) < o2.getProfitMap().get(5)
                && o1.getMaxDrowBack() > o2.getMaxDrowBack()) {
            return 1;
        } else if (o1.getSharpRatio() > o2.getSharpRatio()
                && o1.getProfit() > o2.getProfit()
                && o1.getProfitMap().get(1) > o2.getProfitMap().get(1)
                && o1.getProfitMap().get(3) > o2.getProfitMap().get(3)
                && o1.getProfitMap().get(5) > o2.getProfitMap().get(5)
                && o1.getMaxDrowBack() < o2.getMaxDrowBack()) {
            return -1;
        }
        return 0;
    }
}
