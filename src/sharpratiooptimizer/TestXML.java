/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.File;
import sharpratiooptimizer.configuration.EqConfig;
import sharpratiooptimizer.configuration.EqFiles;

/**
 *
 * @author axjyb
 */
public class TestXML {
    
    public static void main(String[] args) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("EqFiles", EqFiles.class);
        xstream.alias("EqConfig", EqConfig.class);
        xstream.useAttributeFor(EqConfig.class, "name");
        xstream.useAttributeFor(EqConfig.class, "startDate");
        xstream.useAttributeFor(EqConfig.class, "endDate");
        
        EqConfig eqc1 = new EqConfig();
        eqc1.setName("aname");
        eqc1.setFileName("fileName");
        EqConfig eqc2 = new EqConfig();
        eqc2.setName("aname");
        eqc2.setFileName("fileName");
        
        EqFiles eqf = new EqFiles();
        eqf.add(eqc1);
        eqf.add(eqc2);
        
        String xml = xstream.toXML(eqf);
        System.out.println(xml);
        
        EqFiles eqFiles = (EqFiles) xstream.fromXML(new File("src/EqConfigFile.xml"));
        for (EqConfig e : eqFiles.getEqConfigs()) {
            System.out.println(e.getName());
            System.out.println(e.getFileName());
        }
    }
    
}
