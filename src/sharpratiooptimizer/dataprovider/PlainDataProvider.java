/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataprovider;

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
import sharpratiooptimizer.SharpRatioOptimizer;
import sharpratiooptimizer.configuration.ConfigurationHelper;
import sharpratiooptimizer.configuration.EqFileName;
import sharpratiooptimizer.equity.ValueDateSimple;

/**
 *
 * @author axjyb
 */
public class PlainDataProvider implements IDataProvider {

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
    
    private List<EqFileName> list;
    
    public PlainDataProvider() {
        list = ConfigurationHelper.loadConfiguration();
    }
    
    @Override
    public List<ValueDateSimple> getData(String eqName) {
        for ( EqFileName e : list ) {
            if (eqName.equals(e.getName())) {
                return readFromFile(e.getFile());
            }
        }
        return null;
    }
    
    public List<ValueDateSimple> readFromFile(File file) {
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
            
            Collections.sort(list, new Comparator<ValueDateSimple>() {
                @Override
                public int compare(ValueDateSimple t, ValueDateSimple t1) {
                    return (t.getiDate()<t1.getiDate() ? -1 : (t.getiDate()==t1.getiDate()? 0 : 1));
                }
            });
            
        } catch (IOException ex) {
            Logger.getLogger(SharpRatioOptimizer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return list;
    }
    
}
