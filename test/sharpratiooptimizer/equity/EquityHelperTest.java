/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.equity;

import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author axjyb
 */
public class EquityHelperTest {
    
    public EquityHelperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of readFromFile method, of class EquityHelper.
     */
    @Test
    public void testReadFromFile_String() {
//        System.out.println("readFromFile");
//        String fileName = "";
//        EquityHelper instance = new EquityHelper();
//        List expResult = null;
//        List result = instance.readFromFile(fileName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readFromFile method, of class EquityHelper.
     */
    @Test
    public void testReadFromFile_File() {
//        System.out.println("readFromFile");
//        File file = null;
//        EquityHelper instance = new EquityHelper();
//        List expResult = null;
//        List result = instance.readFromFile(file);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDailyReturn method, of class EquityHelper.
     */
    @Test
    public void testGetDailyReturn() {
//        System.out.println("getDailyReturn");
//        List<ValueDateSimple> input = null;
//        EquityHelper instance = new EquityHelper();
//        List expResult = null;
//        List result = instance.getDailyReturn(input);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSharpRatio method, of class EquityHelper.
     */
    @Test
    public void testGetSharpRatio() {
//        System.out.println("getSharpRatio");
//        List<Double> list = null;
//        double expResult = 0.0;
//        double result = EquityHelper.getSharpRatio(list);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of shiftWeighters method, of class EquityHelper.
     */
    @Test
    public void testShiftWeighters() {
//        System.out.println("shiftWeighters");
//        int delta = 6;
//        int sumValue = 100;
//        int[] input = EquityHelper.createWeighters(10, sumValue);
//        int[] result = EquityHelper.shiftWeighters(input, delta, sumValue);
//        
//        int sum = 0;
//        for (int i = 0; i<input.length; i++) {
//            assertFalse(input[i] == result[i]);
//            sum += result[i];
//        }
//        
//        assertEquals(sumValue, sum);
    }

    /**
     * Test of createWeighters method, of class EquityHelper.
     */
    @Test
    public void testCreateWeighters() {
        System.out.println("createWeighters");
        int amount = 4;
        int sumValue = 100;
        EquityHelper instance = new EquityHelper();
        for (int i = 0; i < 1000; i++) {
            int expResult = sumValue;
            int[] result = instance.createWeighters(amount, sumValue);
            int totalResult = 0;
            for (int r : result) {
                totalResult += r;
            }
            assertEquals(expResult, totalResult);
        }
    }

    /**
     * Test of getCompoundSharpRatio method, of class EquityHelper.
     */
    @Test
    public void testGetCompoundSharpRatio() {
//        System.out.println("getCompoundSharpRatio");
//        Equity[] eqs = null;
//        int[] weights = null;
//        double expResult = 0.0;
//        double result = EquityHelper.getCompoundSharpRatio(eqs, weights);
//        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
