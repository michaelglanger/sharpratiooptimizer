/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.equity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author axjyb
 */
public class EquityHelper {
    
    public static List<Double> getDailyReturn(List<ValueDateSimple> input) {
        List<Double> result = new ArrayList<Double>();
        result.add(0d);
        for (int i = 1; i < input.size(); i++) {
            result.add( (input.get(i).getValue()/input.get(i-1).getValue()) -1);
        }
        
        return result;
    }
    
    public static double getSharpRatio(List<Double> list){
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
        
        for(int i = 0; i < input.length; i++) {
            intermediate[i] = (delta * (Math.random()-0.5)) + input[i];
            sum += intermediate[i];
        }
        double d = sumValue / sum / 100;
        
        int iSum = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) Math.round(intermediate[i]*d);
            iSum += result[i];
        }
        
        if (sumValue != iSum) {
            int diff = sumValue - iSum;
            int index = Math.round((float) ((input.length-1)*Math.random()));
            result[index] += diff;
        }
        return result;
    }
    
    public static int[] createWeighters(int amount, int sumValue) {
        int[] result = new int[amount];
        double[] intermediate = new double[amount];
        double sum = 0;
        
        for(int i = 0; i < intermediate.length; i++) {
            intermediate[i] = Math.random();
            sum += intermediate[i];
        }
        double d = sumValue / sum;
        
        int iSum = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) Math.round(intermediate[i]*d);
            iSum += result[i];
        }
        
        if (sumValue != iSum) {
            int diff = sumValue - iSum;
            int index = Math.round((float) ((amount-1)*Math.random()));
            result[index] += diff;
        }
        
        return result;
    }
    
    public static double getCompoundSharpRatio(Equity[] eqs, int[] weights){
            //(List<Double> list1, int dist1, List<Double> list2, int dist2) {
        List<Double> compList = new ArrayList<Double>();
        
        for (int i = 0; i < eqs[0].getList().size(); i++) {
            double sum = 0;
            for ( int j = 0; j < eqs.length; j++) {
                sum += eqs[j].getDailyReturn().get(i)*((float)weights[j]);
            }
            compList.add(sum);
        }
        
        return getSharpRatio(compList);
    }
}
