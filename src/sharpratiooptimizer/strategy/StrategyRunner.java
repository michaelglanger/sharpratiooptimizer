/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.strategy;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 *
 * @author axjyb
 */
public class StrategyRunner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new StrategyModule());
        IStrategy strategy = injector.getInstance(IStrategy.class);
        strategy.setRuns(1000);
        strategy.execute();
    }
}
