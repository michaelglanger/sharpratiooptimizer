/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author michaellanger
 */
public class SharpRatioOptimizer {

    private static final int DATE = 0; 
    private static final int OPEN = 1;
    private static final int HIGH = 2;
    private static final int LOW = 3;       
    private static final int CLOSE = 4;
    private static final int VOLUME = 5;
    private static final int ADJCLOSE = 6;        
    
    private static final int DAY = 2; 
    private static final int MONTH = 1;
    private static final int YEAR = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SharpRatioOptimizer sro = new SharpRatioOptimizer();
        List<ValueDateSimple> listApple = sro.readFromFile("data/apple2011.csv");
        List<ValueDateSimple> listAmex = sro.readFromFile("data/amex2011.csv");
        
        List<Double> appleDRets = sro.getDailyReturn(listApple);
        List<Double> amexDRets = sro.getDailyReturn(listAmex);
        
        double sharpApple = sro.getSharpRatio(appleDRets);
        double sharpAmex = sro.getSharpRatio(amexDRets);
        
        System.out.println("-----Apple-----");
        for (int i = 0; i< listApple.size(); i++) {
            System.out.println(listApple.get(i).getiDate() + " " + listApple.get(i).getValue() + " " + appleDRets.get(i)*100);
        }
        System.out.println(listApple.size());
        
        System.out.println("-----AMEX-----");
        for (int i = 0; i< listAmex.size(); i++) {
            System.out.println(listAmex.get(i).getiDate() + " " + listAmex.get(i).getValue() + " " + amexDRets.get(i)*100);
        }
        System.out.println(listAmex.size());
        
        System.out.println(sharpApple);
        System.out.println(sharpAmex);
                
    }
    
    private double getSharpRatio(List<Double> list){
        StandardDeviation sd = new StandardDeviation();
        Mean mean = new Mean();
        
        double kApple = Math.sqrt(list.size());
        double meanApple = mean.evaluate(getDoubleArray(list));
        double sdApple = sd.evaluate(getDoubleArray(list));
        
        return kApple * (meanApple / sdApple);
    }
    
    private double[] getDoubleArray(List<Double> list) {
        double[] result = new double[list.size()];
        
        for (int i = 0; i < list.size(); i++) {
            result[i] = (double) list.get(i);
        }
        
        return result;
    }
    
    private List<Double> getDailyReturn(List<ValueDateSimple> input) {
        List<Double> result = new ArrayList<Double>();
        result.add(0d);
        for (int i = 1; i < input.size(); i++) {
            result.add( (input.get(i).getValue()/input.get(i-1).getValue()) -1);
        }
        
        return result;
    }
    
    private List<ValueDateSimple> readFromFile(String fileName) {
        File file = new File(fileName);
        System.out.println(file.getAbsolutePath());
        List<ValueDateSimple> list = new ArrayList<ValueDateSimple>();
        try {
            
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if ( Character.isDigit(line.trim().toCharArray()[0]) ) {
                    String[] ss = line.trim().split(",");
                    ValueDateSimple vds = new ValueDateSimple();
                    vds.setValue(Float.parseFloat(ss[ADJCLOSE]));
                    
                    StringBuilder sb = new StringBuilder();
                    String[] ss2 = ss[DATE].trim().split("-");
                    sb.append(ss2[YEAR]).append(ss2[MONTH]).append(ss2[DAY]);
                    vds.setiDate(Integer.parseInt(sb.toString()));
                    
                    list.add(vds);
                } else {
                    System.out.println("Non numeric line: " + line);
                }
            }
            
            Collections.sort(list, new VdsComparator());
            
        } catch (IOException ex) {
            Logger.getLogger(SharpRatioOptimizer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return list;
    }
    
    private class VdsComparator implements Comparator<ValueDateSimple> {

        @Override
        public int compare(ValueDateSimple t, ValueDateSimple t1) {
            return (t.getiDate()<t1.getiDate() ? -1 : (t.getiDate()==t1.getiDate()? 0 : 1));
        }
        
    }
}
