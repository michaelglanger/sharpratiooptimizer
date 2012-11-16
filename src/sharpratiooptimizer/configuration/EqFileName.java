/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.configuration;

import java.io.File;

/**
 *
 * @author axjyb
 */
public class EqFileName {
    
    private File file;
    private String name;
    private String fileName;
    private String symbol;

    public EqFileName(String fileName, String name, String symbol) {
        this.fileName = fileName;
        file = new File(fileName);
        this.name = name;
        this.symbol = symbol;
    }
    
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
   
    public String getFileName() {
        return fileName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
}
