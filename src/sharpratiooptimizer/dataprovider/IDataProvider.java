/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.dataprovider;

import java.util.List;
import sharpratiooptimizer.equity.Equity;
import sharpratiooptimizer.equity.ValueDateSimple;

/**
 *
 * @author axjyb
 */
public interface IDataProvider {
    
    public List<ValueDateSimple> getData(String eqName);
    
}
