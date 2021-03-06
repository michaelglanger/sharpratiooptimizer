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
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import sharpratiooptimizer.configuration.EqConfig;
import sharpratiooptimizer.configuration.EqFiles;
import sharpratiooptimizer.configuration.MarketDescriptor;
import sharpratiooptimizer.configuration.StockDescriptor;
import sharpratiooptimizer.configuration.Stocks;
import sharpratiooptimizer.connection.ConnectionHelper;
import sharpratiooptimizer.dataprovider.YahooStockDataProvider;

/**
 *
 * @author axjyb
 */
public class AuthomaticXMLCreator {

    private static final String init = "http://finance.yahoo.com/d/quotes.csv?s=";
    private static final String end = "&f=sx";
    
    static final Logger log = Logger.getLogger(AuthomaticXMLCreator.class.getName());

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
   
    public void start() {
        BufferedReader in = null;
        Map<String, List> map = new HashMap<String, List>();
        try {
            String url = createURL();
            URLConnection connection = ConnectionHelper.getInstance().getConnection(url);
            //FileNotFoundException
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                log.info(inputLine);
                String[] ss = inputLine.split(",");
                
                String isin = ss[1].replaceAll("\"", "");
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
        
        EqFiles eqf = new EqFiles();
        List<EqConfig> eqcs = new ArrayList<EqConfig>();
        eqf.setEqConfigs(eqcs);
        
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
                
                EqConfig ec = new EqConfig();
                ec.setName((ss+"_"+s).toLowerCase());
                ec.setSymbol(ss);
                eqcs.add(ec);
            }
        }
        
        
        
        
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("Stocks", Stocks.class);
        xstream.alias("MarketDescriptor", MarketDescriptor.class);
        xstream.alias("StockDescriptor", StockDescriptor.class);
        xstream.useAttributeFor(MarketDescriptor.class, "id");
        xstream.useAttributeFor(StockDescriptor.class, "id");
        xstream.useAttributeFor(StockDescriptor.class, "market");
        
        xstream.alias("EqFiles", EqFiles.class);
        xstream.alias("EqConfig", EqConfig.class);
        xstream.useAttributeFor(EqConfig.class, "name");
        xstream.useAttributeFor(EqConfig.class, "startDate");
        xstream.useAttributeFor(EqConfig.class, "endDate");
        
        FileWriter fw;
        try {
            fw = new FileWriter("./src/stocks.xml");
            xstream.toXML(stocks, fw);
            
            fw = new FileWriter("./src/EqConfigFile.xml");
            xstream.toXML(eqf, fw);
        } catch (IOException ex) {
            Logger.getLogger(AuthomaticXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
