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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    private static final int DAY = 1; 
    private static final int MONTH = 0;
    private static final int YEAR = 2;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    private void readFromFile(String fileName) {
        File file = new File(fileName);
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
                    String[] ss2 = ss[DATE].trim().split("/");
                    sb.append("20").append(ss2[YEAR]).append(ss2[MONTH]).append(ss2[DAY]);
                    Integer.parseInt(sb.toString());
                    
                } else {
                    System.out.println("Non numeric line: " + line);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SharpRatioOptimizer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
}
