/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   ContinuousVariation.java
 */
package sgavariationanalysis.continuous;

import java.util.ArrayList;
import java.util.Random;
import sgavariationanalysis.SGAVariationAnalysis;

/**
 *
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class ContinuousVariation {
    
    
    private static final Random RAND = SGAVariationAnalysis.RAND;
    private static final float CROSSOVER_PROB = 
            SGAVariationAnalysis.CROSSOVER_PROB;
    private static final float MUTATION_PROB = 
            SGAVariationAnalysis.MUTATION_PROB;
    
    
/*================================== Constants ===============================*/


    /* An id for use with the whole arithmetic crossover */
    public static final int WAC = 1;
    
    /* An id for use with the local arithmetic crossover */
    public static final int LAC = 2;
    
    
/*============================= Crossover Methods ============================*/


    /**
     * Returns a list containing the two children generated from the given
     * parents using a whole arithmetic crossover method.
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @param isLocal the flag for using local variation
     * @return a list containing two children
     */
    public static ArrayList<ContinuousIndividual> arithmeticCrossover(
            ContinuousIndividual parentA,
            ContinuousIndividual parentB,
            boolean isLocal) {
        
        ArrayList<ContinuousIndividual> res = new ArrayList<>(2);
        ArrayList<Float> chromoChildA = parentA.getChromosome();
        ArrayList<Float> chromoChildB = parentB.getChromosome();
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
            
            float a = RAND.nextFloat();
            float b = 1.0f - a;
            
            for (int i = 0; i < chromoChildA.size(); i++) {
                
                float x1 = chromoChildA.get(i);
                float x2 = chromoChildA.get(i);
                
                if (isLocal) {
                    a = RAND.nextFloat();
                    b = 1.0f - a;
                }
                
                float y1 = a * x1 + b * x2;
                float y2 = b * x1 + a * x2;
                
                chromoChildA.set(i, y1);
                chromoChildB.set(i, y2);
            }
        }
        
        res.add(0, new ContinuousIndividual(chromoChildA, parentA));
        res.add(1, new ContinuousIndividual(chromoChildB, parentB));
        
        return res;
    }
    
    /**
     * Performs a mutation on the given individual.
     * 
     * @param individual the individual to be mutated
     */
    public static void uniformMutation(ContinuousIndividual individual) {
        
        for (int i = 0; i < individual.getChromosome().size(); i++) {
            if (RAND.nextFloat() < MUTATION_PROB) {
                float a = RAND.nextFloat();
                float l = individual.getTestFunction().getXLowerBound();
                float u = individual.getTestFunction().getXUpperBound();
                
                individual.setVar(i,  a * (u - l) + l);
            }
        }
    }
}
