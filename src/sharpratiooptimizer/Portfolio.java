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
    
    public Portfolio(Equity[] eqs, int[] wts) {
        ll = eqs;
        weighters = wts;
        calculateSharpRatio();
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
    
    public double calculateSharpRatio() {
        sharpRatio = EquityHelper.getCompoundSharpRatio(ll, weighters);
        return sharpRatio;
    }
    
    public double getSharpRatio() {
        return sharpRatio;
    }
    
    public int[] getWeighters() {
        return weighters;
    }
    
    public void setWeighters(int[] ws) {
        weighters = ws;
    }
    
}
