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

    public EqFileName(String fileName, String name) {
        file = new File(fileName);
        this.name = name;
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
   
    
}
