/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author axjyb
 */
public class ConfigurationHelper {
    
    public static List<EqFileName> loadConfiguration() {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("EqFiles", EqFiles.class);
        xstream.alias("EqConfig", EqConfig.class);
        xstream.useAttributeFor(EqConfig.class, "name");
        xstream.useAttributeFor(EqConfig.class, "startDate");
        xstream.useAttributeFor(EqConfig.class, "endDate");
        
        EqFiles eqFiles = (EqFiles) xstream.fromXML(new File("src/EqConfigFile.xml"));
        String dataFolder = eqFiles.getDataFolder();
        String fileFormat = eqFiles.getFileFormat();
        
        List<EqFileName> result = new ArrayList<EqFileName>();
        for (EqConfig e : eqFiles.getEqConfigs()) {
            EqFileName efn = new EqFileName(dataFolder+e.getFileName()+"."+fileFormat, e.getName(), e.getSymbol());
            result.add(efn);
        }
        return result;
    }
    
    public static Stocks loadStocksConfiguration() {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("Stocks", Stocks.class);
        xstream.alias("MarketDescriptor", MarketDescriptor.class);
        xstream.alias("StockDescriptor", StockDescriptor.class);
        xstream.useAttributeFor(MarketDescriptor.class, "id");
        xstream.useAttributeFor(StockDescriptor.class, "id");
        xstream.useAttributeFor(StockDescriptor.class, "market");
        
        Stocks stocks = (Stocks) xstream.fromXML(new File("src/stocks.xml"));
        
        
        return stocks;
    }
    
}
