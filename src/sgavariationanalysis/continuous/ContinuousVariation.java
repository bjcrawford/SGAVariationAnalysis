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
    
    /* An id for use with the linear crossover */
    public static final int LC = 3;
    
    /* An id for use with the heuristic crossover */
    public static final int HC = 4;
    
    /* An id for use with the blend crossover */
    public static final int BC = 5;
    
    
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
                float x2 = chromoChildB.get(i);
                
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
     * Returns a list containing the two children generated from the given
     * parents using a linear crossover method.
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @return a list containing two children
     */
    public static ArrayList<ContinuousIndividual> linearCrossover(
            ContinuousIndividual parentA,
            ContinuousIndividual parentB) {
        
        ArrayList<ContinuousIndividual> res = new ArrayList<>(2);
        ContinuousIndividual childA = new ContinuousIndividual(parentA);
        ContinuousIndividual childB = new ContinuousIndividual(parentB);
        ContinuousIndividual childC;
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
            
            ArrayList<Float> chromoChildA = parentA.getChromosome();
            ArrayList<Float> chromoChildB = parentB.getChromosome();
            ArrayList<Float> chromoChildC = new ArrayList<>();
            
            float a = RAND.nextFloat();
            float b = 1.0f - a;
            
            for (int i = 0; i < chromoChildA.size(); i++) {
                
                int l = parentA.getTestFunction().getXLowerBound();
                int u = parentA.getTestFunction().getXLowerBound();
                
                float x1 = chromoChildA.get(i);
                float x2 = chromoChildB.get(i);
                
                float y1 = 0.5f * x1 + 0.5f * x2;
                float y2 = 1.5f * x1 - 0.5f * x2;
                float y3 = - 0.5f * x1 + 1.5f * x2;
                
                if (y2 <= l || y2 >= u) {
                    y2 = y1;
                }
                if (y3 <= l || y3 >= u) {
                    y3 = y1;
                }
                
                chromoChildA.set(i, y1);
                chromoChildB.set(i, y2);
                chromoChildC.add(i, y3);
            }
            
            
            childA = new ContinuousIndividual(chromoChildA, parentA);
            childB = new ContinuousIndividual(chromoChildB, parentA);
            childC = new ContinuousIndividual(chromoChildC, parentA);
            
            float totalFitness = childA.getFitTransValue() +
                    childB.getFitTransValue() + childC.getFitTransValue();
            
            float relFitA = childA.getFitTransValue() / totalFitness;
            float relFitB = childB.getFitTransValue() / totalFitness;
            float relFitC = childC.getFitTransValue() / totalFitness;
            
            if (relFitA <= relFitB && relFitA <= relFitC) {
                childA = childB;
                childB = childC;
            }
            else if (relFitB <= relFitA && relFitB <= relFitC) {
                childB = childC;
            }
            
        }
        
        res.add(0, childA);
        res.add(1, childB);
        
        return res;
    }
    
    /**
     * Returns a list containing the two children generated from the given
     * parents using a heuristic crossover method.
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @return a list containing two children
     */
    public static ArrayList<ContinuousIndividual> heuristicCrossover(
            ContinuousIndividual parentA,
            ContinuousIndividual parentB) {
        
        ArrayList<ContinuousIndividual> res = new ArrayList<>(2);
        ContinuousIndividual childA = new ContinuousIndividual(parentA);
        ContinuousIndividual childB = new ContinuousIndividual(parentB);
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
            
            ArrayList<Float> chromoChildA = parentA.getChromosome();
            ArrayList<Float> chromoChildB = parentB.getChromosome();
            
            do {
                
                float b1 = RAND.nextFloat() * 0.4f + 0.8f;
                for (int i = 0; i < chromoChildA.size(); i++) {
                    float x1 = chromoChildA.get(i);
                    float x2 = chromoChildB.get(i);
                    float y;
                    
                    if (parentA.getFitTransValue() > parentB.getFitTransValue()) {
                        y = x2 + b1 * (x1 - x2);
                    }
                    else {
                        y = x1 + b1 * (x2 - x1);
                    }
                    chromoChildA.set(i, y);
                    childA = new ContinuousIndividual(chromoChildA, parentA);
                } 
            } while (!childA.isInBounds());
            
            do {
                
                float b2 = RAND.nextFloat() * 0.4f + 0.8f;
                for (int i = 0; i < chromoChildA.size(); i++) {
                    float x1 = chromoChildA.get(i);
                    float x2 = chromoChildB.get(i);
                    float y;
                    
                    if (parentA.getFitTransValue() > parentB.getFitTransValue()) {
                        y = x2 + b2 * (x1 - x2);
                    }
                    else {
                        y = x1 + b2 * (x2 - x1);
                    }
                    chromoChildB.set(i, y);
                    childB = new ContinuousIndividual(chromoChildB, parentB);
                } 
            } while (!childB.isInBounds());
        }
        
        res.add(0, childA);
        res.add(1, childB);
        
        return res;
    }
    
    /**
     * Returns a list containing the two children generated from the given
     * parents using a blend crossover method.
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @return a list containing two children
     */
    public static ArrayList<ContinuousIndividual> blendCrossover(
            ContinuousIndividual parentA,
            ContinuousIndividual parentB) {
        
        ArrayList<ContinuousIndividual> res = new ArrayList<>(2);
        ContinuousIndividual childA = new ContinuousIndividual(parentA);
        ContinuousIndividual childB = new ContinuousIndividual(parentB);
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
            
            ArrayList<Float> chromoChildA = parentA.getChromosome();
            ArrayList<Float> chromoChildB = parentB.getChromosome();
            
            float a = 0.50f;
            
            for (int i = 0; i < chromoChildA.size(); i++) {

                if (chromoChildA.get(i) > chromoChildB.get(i)) {
                    ArrayList<Float> chromoTemp = chromoChildA;
                    chromoChildA = chromoChildB;
                    chromoChildB = chromoTemp;
                }

                float x1 = chromoChildA.get(i);
                float x2 = chromoChildB.get(i);
                float l = x1 - a * (x2 - x1);
                float u = x2 + a * (x2 - x1);
                float y;

                do {
                    y = RAND.nextFloat() * (u - l) + l;
                } while (y <= parentA.getTestFunction().getXLowerBound() ||
                        y >= parentA.getTestFunction().getXUpperBound());

                chromoChildA.set(i, y);
            } 

            childA = new ContinuousIndividual(chromoChildA, parentA);
            
            for (int i = 0; i < chromoChildB.size(); i++) {

                if (chromoChildB.get(i) > chromoChildA.get(i)) {
                    ArrayList<Float> chromoTemp = chromoChildB;
                    chromoChildB = chromoChildA;
                    chromoChildA = chromoTemp;
                }

                float x1 = chromoChildB.get(i);
                float x2 = chromoChildA.get(i);
                float l = x1 - a * (x2 - x1);
                float u = x2 + a * (x2 - x1);
                float y;

                do {
                    y = RAND.nextFloat() * (u - l) + l;
                } while (y <= parentB.getTestFunction().getXLowerBound() ||
                        y >= parentB.getTestFunction().getXUpperBound());


                chromoChildB.set(i, y);
            } 
            
            childB = new ContinuousIndividual(chromoChildB, parentB);
        }
        
        res.add(0, childA);
        res.add(1, childB);
        
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
