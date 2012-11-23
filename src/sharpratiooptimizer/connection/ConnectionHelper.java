/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import sharpratiooptimizer.dataprovider.YahooStockDataProvider;

/**
 *
 * @author axjyb
 */
public class ConnectionHelper {
    
    private static ConnectionHelper instance;
    private static String proxy;
    private static String port;
    
    private ConnectionHelper() {
        ResourceBundle rb = ResourceBundle.getBundle("configuration");
        proxy = rb.getString("proxy");
        port = rb.getString("proxy_port");
    }
    
    public static ConnectionHelper getInstance() {
        if (instance == null) {
            instance = new ConnectionHelper();
        }
        return instance;
    }
    
     public URLConnection getConnection(String url) {
        URLConnection yc = null;
        try {
            URL oracle = new URL(url);
            if (!"".equals(proxy) && !"".equals(port)) {
                Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, Integer.parseInt(port)));
                yc = oracle.openConnection(p);
            } else {
                yc = oracle.openConnection();
            }
        } catch (IOException ex) {
            Logger.getLogger(YahooStockDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return yc;
    }
    
}
