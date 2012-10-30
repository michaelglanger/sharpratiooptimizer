/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer;

/**
 *
 * @author Michael G. Langer
 */
public class Portfolio {
    
    private Equity[] ll;
    private int[] weighters;
    private double sharpRatio;
    boolean shCalculated = false;
    
    public Portfolio(Equity[] eqs, int[] wts) {
        ll = eqs;
        weighters = wts;
        getSharpRatio();
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-- Portfolio --");
        for (int i = 0; i < ll.length; i++) {
            sb.append("\n"+ll[i].getName()+ " - " + weighters[i] + " Sharp ratio: " + ll[i].getSharpRatio());;
        }
        sb.append("\nSharp Ratio: "+sharpRatio);
        return sb.toString();
    }
    
    public double getSharpRatio() {
        if (!shCalculated) {
            sharpRatio = EquityHelper.getCompoundSharpRatio(ll, weighters);
            shCalculated = true;
        } 
        return sharpRatio;
    }
    
}
