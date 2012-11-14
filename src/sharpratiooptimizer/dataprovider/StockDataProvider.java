/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataprovider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sharpratiooptimizer.equity.ValueData;
import sharpratiooptimizer.equity.ValueDataStock;

/**
 *
 * @author axjyb
 */
public class StockDataProvider implements IDataProvider{

    @Override
    public List<ValueData> getData(String eqName) {
        return readFromFile(eqName);
    }
    
    public List<ValueData> readFromFile(String fileName) {
        File file = new File(fileName);
        System.out.println(file.getAbsolutePath());
        List<ValueData> list = new ArrayList<ValueData>();
        try {
            
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if ( Character.isDigit(line.trim().toCharArray()[0]) ) {
                    String[] ss = line.trim().split(",");
                    ValueDataStock vds = new ValueDataStock();
                    
                    vds.setOpen(new BigDecimal(ss[OPEN]));
                    vds.setHigh(new BigDecimal(ss[HIGH]));
                    vds.setLow(new BigDecimal(ss[LOW]));
                    vds.setClose(new BigDecimal(ss[CLOSE]));
                    vds.setVolume(Long.parseLong(ss[VOLUME]));
                    vds.setAdjclose(new BigDecimal(ss[ADJCLOSE]));
                    
                    StringBuilder sb = new StringBuilder();
                    String[] ss2 = ss[DATE].trim().split("-");
                    sb.append(ss2[YEAR]).append(ss2[MONTH]).append(ss2[DAY]);
                    
                    vds.setiDate(Integer.parseInt(sb.toString()));
                    
                    vds.setDate(Integer.parseInt(ss2[YEAR]), Integer.parseInt(ss2[MONTH]), Integer.parseInt(ss2[DAY]));
                    
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
            Logger.getLogger(StockDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return list;
    }

    @Override
    public List<ValueData> getData(String symbol, Date initDate, Date endDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
