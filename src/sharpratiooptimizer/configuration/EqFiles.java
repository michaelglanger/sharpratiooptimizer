/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author axjyb
 */
public class EqFiles {
    
    String dataFolder;
    String fileFormat;
    List<EqConfig> eqConfigs = new ArrayList<EqConfig>();

    public List<EqConfig> getEqConfigs() {
        return eqConfigs;
    }

    public void setEqConfigs(List<EqConfig> eqFiles) {
        this.eqConfigs = eqFiles;
    }
    
    public void add(EqConfig entry) {
                eqConfigs.add(entry);
        }

    public List getContent() {
            return eqConfigs;
    }

    public String getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(String dataFolder) {
        this.dataFolder = dataFolder;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
    
    
    
}
