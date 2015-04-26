/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   BinaryIndividualTest.java
 */
package sgavariationanalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sgavariationanalysis.gatestfunction.Function1;
import sgavariationanalysis.gatestfunction.GATestFunction;

/**
 *
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class BinaryIndividualTest {
    
    
    public BinaryIndividualTest() {
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
    
    @Test
    public void testDefaultConstructor() {
        
        GATestFunction func1 = new Function1();
        
        BinaryIndividual individual = new BinaryIndividual(
                func1, new Random(), false);
        
        assertTrue(individual.getRealValue().get(0) > func1.getXLowerBound());
        assertTrue(individual.getRealValue().get(0) < func1.getXUpperBound());
    }
    
    @Test
    public void testChromosomeConstructor() {
        
        Boolean[] c = new Boolean[] { true, true, false, true, false, true, 
			              true, false, false, true, false, true };
        
        ArrayList<Boolean> chromosome = new ArrayList<>();
        chromosome.addAll(Arrays.asList(c));
        
        GATestFunction func1 = new Function1();
        
        BinaryIndividual individual = new BinaryIndividual(chromosome,
                new BinaryIndividual(func1, new Random(), false));
        
        assertEquals(0.953369f, (float) individual.getRealValue().get(0), 0.0008f);
        assertEquals(1.051966f, individual.getObjValue(), 0.0008f);
        assertEquals("101001101011", individual.getGenotype());
        assertTrue(individual.getRealValue().get(0) > func1.getXLowerBound());
        assertTrue(individual.getRealValue().get(0) < func1.getXUpperBound());
    }
    
}
