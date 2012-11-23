/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.strategy;

import com.google.inject.AbstractModule;
import java.util.Comparator;
import sharpratiooptimizer.strategy.comparators.SharpProfitBackComparator3;
import sharpratiooptimizer.strategy.sharpRatio.SharpRatioStrategy2;

/**
 *
 * @author axjyb
 */
public class StrategyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Comparator.class).to(SharpProfitBackComparator3.class);
        bind(IStrategy.class).to(SharpRatioStrategy2.class);
    }
    
}
