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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sharpratiooptimizer.SharpRatioOptimizer;
import sharpratiooptimizer.configuration.ConfigurationHelper;
import sharpratiooptimizer.configuration.EqFileName;
import sharpratiooptimizer.equity.ValueData;
import sharpratiooptimizer.equity.ValueDateSimple;

/**
 *
 * @author axjyb
 */
public class PlainDataProvider implements IDataProvider {
    
    private List<EqFileName> list;
    
    public PlainDataProvider() {
        list = ConfigurationHelper.loadConfiguration();
    }
    
    @Override
    public List<ValueData> getData(String eqName) {
        for ( EqFileName e : list ) {
            if (eqName.equals(e.getName())) {
                return readFromFile(e.getFile());
            }
        }
        return null;
    }
    
    public List<ValueData> readFromFile(File file) {
        System.out.println(file.getAbsolutePath());
        List<ValueData> list = new ArrayList<ValueData>();
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
            
            Collections.sort(list, new Comparator<ValueData>() {
                @Override
                public int compare(ValueData t, ValueData t1) {
                    return (t.getiDate()<t1.getiDate() ? -1 : (t.getiDate()==t1.getiDate()? 0 : 1));
                }
            });
            
        } catch (IOException ex) {
            Logger.getLogger(PlainDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return list;
    }

    @Override
    public List<ValueData> getData(String symbol, Date initDate, Date endDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
