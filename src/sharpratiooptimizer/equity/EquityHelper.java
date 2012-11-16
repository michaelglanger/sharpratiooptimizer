/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.equity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author axjyb
 */
public class EquityHelper {

    public static List<Double> getDailyReturn(List<ValueData> input) {
        List<Double> result = new ArrayList<Double>();
        result.add(0d);
        for (int i = 1; i < input.size(); i++) {
            result.add((input.get(i).getMainData().doubleValue() / input.get(i - 1).getMainData().doubleValue()) - 1);
        }

        return result;
    }
    
    public static Map<Integer, Double> calculateDailyReturn(List<ValueData> input) {
        Map<Integer, Double> result = new HashMap<Integer, Double>();
        
        ValueData vd = input.get(0);
        result.put(vd.getiDate(), new Double(0));
        
        for (int i = 1; i < input.size(); i++) {
            double d = ((input.get(i).getMainData().doubleValue() / input.get(i - 1).getMainData().doubleValue()) - 1);
            result.put(input.get(i).getiDate(), d);
        }
        
        return result;
    }

    public static double getSharpRatio(List<Double> list) {
        StandardDeviation standardDeviation = new StandardDeviation();
        Mean mean = new Mean();

        double k = Math.sqrt(list.size());
        double m = mean.evaluate(getDoubleArray(list));
        double sd = standardDeviation.evaluate(getDoubleArray(list));

        return k * (m / sd);
    }

    private static double[] getDoubleArray(List<Double> list) {
        double[] result = new double[list.size()];

        for (int i = 0; i < list.size(); i++) {
            result[i] = (double) list.get(i);
        }

        return result;
    }

    public static int[] shiftWeighters(int[] input, int delta, int sumValue) {
        int[] result = new int[input.length];

        double[] intermediate = new double[input.length];
        double sum = 0;

        for (int i = 0; i < input.length; i++) {
            intermediate[i] = (delta * (Math.random() - 0.5)) + input[i];
            sum += intermediate[i];
        }
        double d = sumValue / sum / 100;

        int iSum = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) Math.round(intermediate[i] * d);
            iSum += result[i];
        }

        if (sumValue != iSum) {
            int diff = sumValue - iSum;
            int index = Math.round((float) ((input.length - 1) * Math.random()));
            result[index] += diff;
        }
        return result;
    }

    public static int[] createWeighters(int amount, int sumValue) {
        int[] result = new int[amount];
        double[] intermediate = new double[amount];
        double sum = 0;

        for (int i = 0; i < intermediate.length; i++) {
            intermediate[i] = Math.random();
            sum += intermediate[i];
        }
        double d = sumValue / sum;

        int iSum = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) Math.round(intermediate[i] * d);
            iSum += result[i];
        }

        if (sumValue != iSum) {
            int diff = sumValue - iSum;
            int index = Math.round((float) ((amount - 1) * Math.random()));
            result[index] += diff;
        }

        return result;
    }

    
    public static List<Integer> getDatesList(Equity[] eqs) {
        Set<Integer> sResult = new HashSet<Integer>();
        
        for (Equity e : eqs) {
            for (ValueData vd : e.getList()) {
                sResult.add(vd.getiDate());
            }
        }
        
        List<Integer> result = new ArrayList<Integer>();
        
        result.addAll(sResult);
        
        Collections.sort(result, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (o1<o2 ? -1 : (o1==o2 ? 0 : 1));
            }
        });
        
        return result;
    }
    
    public static double getCompoundSharpRatio(Equity[] eqs, int[] weights) {
        //(List<Double> list1, int dist1, List<Double> list2, int dist2) {
        List<Double> compList = new ArrayList<Double>();

        List<Integer> datesList = getDatesList(eqs);
        List<Double> dailyReturns = new ArrayList<Double>();
        for (Integer i : datesList) {
            double sum = 0;
            for (int j = 0; j < eqs.length; j++) {
                Map<Integer, Double> dr = eqs[j].getdReturns();
                Double d = dr.get(i);
                if (d==null) {
                    d = 0d;
                } else {
                    sum += d * ((float) weights[j]);
                }
            }
            dailyReturns.add(sum);
        }

        return getSharpRatio(dailyReturns);
        
//        for (int i = 0; i < eqs[0].getList().size(); i++) {
//            double sum = 0;
//            for (int j = 0; j < eqs.length; j++) {
//                try {
//                    sum += eqs[j].getDailyReturn().get(i) * ((float) weights[j]);
//                } catch (IndexOutOfBoundsException e) {
//                    System.out.println(eqs[j].getName());
//                    e.printStackTrace();
//                }
//            }
//            compList.add(sum);
//        }
//
//
//        return getSharpRatio(compList);
    }
}
