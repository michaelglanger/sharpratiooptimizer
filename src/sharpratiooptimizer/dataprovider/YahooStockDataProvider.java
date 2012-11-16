/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sharpratiooptimizer.equity.ValueData;
import sharpratiooptimizer.equity.ValueDataStock;

/**
 *
 * @author axjyb
 */
public class YahooStockDataProvider implements IDataProvider {

    private static final String init = "http://ichart.finance.yahoo.com/table.csv?s=";
    private static final String initMonth = "&a=";
    private static final String initDay = "&b=";
    private static final String initYear = "&c="; 
    private static final String endMonth = "&d=";
    private static final String endDay = "&e=";
    private static final String endYear = "&f=";
    private static final String end = "&g=d&ignore=.csv";
    
    
    @Override
    public List<ValueData> getData(String eqName) {
        return null;
    }
    
    @Override
    public List<ValueData> getData(String eqName, Calendar initDate, Calendar endDate) {
       return readFromYahoo(eqName, initDate, endDate);
    }
        
    public List<ValueData> readFromYahoo(String symbol, Calendar initDate, Calendar endDate) {
        List<ValueData> list = new ArrayList<ValueData>();
        BufferedReader in = null;
        try {
            String url = createURL(symbol, initDate, endDate);
            URLConnection connection = getConnection(url);
            //FileNotFoundException
            in = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                
                 if ( Character.isDigit(inputLine.trim().toCharArray()[0]) ) {
                    String[] ss = inputLine.trim().split(",");
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
                    System.out.println("Non numeric line: " + inputLine);
                }
                
            }
            in.close();
            
            Collections.sort(list, new Comparator<ValueData>() {
                @Override
                public int compare(ValueData t, ValueData t1) {
                    return (t.getiDate()<t1.getiDate() ? -1 : (t.getiDate()==t1.getiDate()? 0 : 1));
                }
            });
            
        } catch (IOException ex) {
            Logger.getLogger(YahooStockDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(YahooStockDataProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }
    
    
    public String createURL(String symbol, Calendar initDate, Calendar endDate) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(init).append(symbol)
          .append(initMonth).append(initDate.get(Calendar.MONTH))
          .append(initDay).append(initDate.get(Calendar.DAY_OF_MONTH))
          .append(initYear) .append(normalizeYear(initDate.get(Calendar.YEAR)))
          .append(endMonth).append(endDate.get(Calendar.MONTH))
          .append(endDay).append(endDate.get(Calendar.DAY_OF_MONTH))
          .append(endYear).append(normalizeYear(endDate.get(Calendar.YEAR)))
          .append(end);
        
        return sb.toString();
    }
    
    private URLConnection getConnection(String url) {
        URLConnection yc = null;
        try {
            URL oracle = new URL(url);
            Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.ict", 8080));
              
            yc = oracle.openConnection(p);
        } catch (IOException ex) {
            Logger.getLogger(YahooStockDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return yc;
    }
    
    private int normalizeYear(int year) {
        if (year < 1900) {
            return year+1900;
        }
        return year;
    }
}
