/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.xmlcreator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import sharpratiooptimizer.configuration.MarketDescriptor;
import sharpratiooptimizer.configuration.StockDescriptor;
import sharpratiooptimizer.configuration.Stocks;
import sharpratiooptimizer.dataprovider.YahooStockDataProvider;

/**
 *
 * @author axjyb
 */
public class AuthomaticXMLCreator {

    private static final String init = "http://finance.yahoo.com/d/quotes.csv?s=";
    private static final String end = "&f=snx";

    public String createURL() {
        ResourceBundle rb = ResourceBundle.getBundle("symbols");
        String symbols = rb.getString("symbols");

        String[] ss = symbols.split(",");

        StringBuilder sb = new StringBuilder();

        sb.append(init);

        for (String s : ss) {
            sb.append(s.trim()).append("+");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(end);

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

    public void start() {
        BufferedReader in = null;
        Map<String, List> map = new HashMap<String, List>();
        try {
            String url = createURL();
            URLConnection connection = getConnection(url);
            //FileNotFoundException
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                String[] ss = inputLine.split(",");
                
                String isin = ss[2].replaceAll("\"", "");
                if (map.containsKey(isin)) {
                    map.get(isin).add(ss[0].replaceAll("\"", ""));
                } else {
                    List<String> l = new ArrayList<String>();
                    l.add(ss[0].replaceAll("\"", ""));
                    map.put(isin, l);
                }
                
            }
            
            createXML(map);
        } catch (IOException ex) {
            Logger.getLogger(YahooStockDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in!=null)
                    in.close();
            } catch (IOException ex) {
                Logger.getLogger(YahooStockDataProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void createXML(Map<String, List> map) {
        Stocks stocks = new Stocks();
        List<MarketDescriptor> mds = new ArrayList<MarketDescriptor>();
        List<StockDescriptor> sdr = new ArrayList<StockDescriptor>();
        
        stocks.setMarkets(mds);
        stocks.setStockList(sdr);
        
        for (String s : map.keySet()) {
            MarketDescriptor md = new MarketDescriptor();
            md.setName(s);
            md.setId(s.toLowerCase());
            mds.add(md);
            
            List<String> ml = map.get(s);
            for(String ss : ml) {
                StockDescriptor sd = new StockDescriptor();
                sd.setMarket(s);
                sd.setName(ss);
                sd.setId((ss+"_"+s).toLowerCase());
                sdr.add(sd);
            }
        }
        
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("Stocks", Stocks.class);
        xstream.alias("MarketDescriptor", MarketDescriptor.class);
        xstream.alias("StockDescriptor", StockDescriptor.class);
        xstream.useAttributeFor(MarketDescriptor.class, "id");
        xstream.useAttributeFor(StockDescriptor.class, "id");
        xstream.useAttributeFor(StockDescriptor.class, "market");
        
        FileWriter fw;
        try {
            fw = new FileWriter("./src/stocks2.xml");
            xstream.toXML(stocks, fw);
        } catch (IOException ex) {
            Logger.getLogger(AuthomaticXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
