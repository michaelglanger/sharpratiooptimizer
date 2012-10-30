/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author michaellanger
 */
public class SharpRatioOptimizer {
 
    private static final String DATA_FOLDER ="dataFolder";
    private static final String FILE_FORMAT = "fileFormat";
    private static final String EQUITIES = "equities";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ResourceBundle rb = ResourceBundle.getBundle("equities");
        
        String folder = rb.getString(DATA_FOLDER);
        String fileFormat = rb.getString(FILE_FORMAT);
        String equitiesString = rb.getString(EQUITIES);
        
        String[] equitiesFiles = equitiesString.split(",");
        
        String[] files = new String[equitiesFiles.length];
        
        for(int i = 0; i < files.length; i++) {
            files[i] = ""+folder + equitiesFiles[i] + fileFormat;
        }
        
        SharpRatioOptimizer sro = new SharpRatioOptimizer();
        
//        Portfolio p = PortfolioHelper.createPortofolio(files);
//        System.out.println(p.toString());
        
        List<Portfolio> pps = PortfolioHelper.createSetPortfolio(files, 4, 10);
        
        for ( int k = 0; k < 100000; k++) {
            Collections.sort(pps, new Comparator<Portfolio>() {

                @Override
                public int compare(Portfolio o1, Portfolio o2) {
                    if (o1.getSharpRatio() < o2.getSharpRatio() ) {
                        return 1;
                    } else if (o1.getSharpRatio() > o2.getSharpRatio() ) {
                        return -1;
                    }
                    return 0;
                }

            });

            for ( int i = 9; i >= 5; i--) {
               pps.remove(i);
            }

            pps.addAll(PortfolioHelper.createSetPortfolio(files, 4, 5));
        }
        
         for (Portfolio pp : pps) {
            System.out.println(pp.toString());
        }
         
         System.out.println("-------------------");
         System.out.println(pps.get(0).toString());
        
    }
    
    
    
}
