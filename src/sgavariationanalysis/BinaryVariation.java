/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   VariationMethods.java
 */
package sgavariationanalysis;

import java.util.ArrayList;

/**
 *
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class BinaryVariation {
    
    
/*================================== Constants ===============================*/


    /* An id for use with the single point crossover */
    public static final int SPC = 1;
    
    /* An id for use with the dual point crossover */
    public static final int DPC = 2;
    
    /* An id for use with the single point crossover with reduced surrogate */
    public static final int SPCRS = 3;
    
    /* An id for use with the dual point crossover with reduced surrogate */
    public static final int DPCRS = 4;
    
    /* An id for use with the ring crossover */
    public static final int RC = 5;
    
    
/*============================= Crossover Methods ============================*/


    /**
     * Returns a list containing the two children generated from the given
     * parents using a single point crossover method. This method can be
     * made to use a reduced surrogate optimization by passing the the
     * flag as true.
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @param reducedSurrogate a flag for reduced surrogate
     * @return a list containing two children
     */
    public static ArrayList<BinaryIndividual> singlePointCrossover(
            BinaryIndividual parentA,
            BinaryIndividual parentB,
            boolean reducedSurrogate) {
        
        int numGenes = parentA.getGenesPerVar() * parentA.getNumVars();
        int lowerBound = 1;
        int upperBound = numGenes - 1;
        int crossoverPoint;
        ArrayList<BinaryIndividual> res = new ArrayList<>(2);
        ArrayList<Boolean> chromoChildA = parentA.getChromosome();
        ArrayList<Boolean> chromoChildB = parentB.getChromosome();
        
        if (reducedSurrogate) {
            for (int i = lowerBound; i < upperBound; i++) {
                if (chromoChildA.get(i) ^ chromoChildB.get(i)) {
                    lowerBound = i;
                    break;
                }
            }
            for (int i = upperBound; i > lowerBound; i--) {
                if (chromoChildA.get(i-1) ^ chromoChildB.get(i-1)) { 
                    upperBound = i;
                    break;
                }
            }
            if (lowerBound >= upperBound) { // identical chromsomes
                lowerBound = 0;
                upperBound = numGenes - 1;
            }
        }
        
        do {
            crossoverPoint = (int) (SGAVariationAnalysis.RAND.nextFloat() * numGenes);
        } while (crossoverPoint < lowerBound || crossoverPoint > upperBound);
        
        if (SGAVariationAnalysis.RAND.nextFloat() < SGAVariationAnalysis.CROSSOVER_PROB) {
            
            for (int j = 0; j < numGenes; j++) {
                if (j > crossoverPoint) {
                    chromoChildA.set(j, parentB.getChromosome().get(j));
                    chromoChildB.set(j, parentA.getChromosome().get(j));
                }
            }
        }
        
        res.add(0, new BinaryIndividual(chromoChildA, parentA.getFitFunction()));
        res.add(1, new BinaryIndividual(chromoChildB, parentB.getFitFunction()));
        
        return res;
    }
    
    /**
     * Returns a list containing the two children generated from the given
     * parents using a dual point crossover method. This method can be
     * made to use a reduced surrogate optimization by passing the the
     * flag as true.
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @param reducedSurrogate a flag for reduced surrogate
     * @return a list containing two children
     */
    public static ArrayList<BinaryIndividual> dualPointCrossover(
            BinaryIndividual parentA,
            BinaryIndividual parentB,
            boolean reducedSurrogate) {
        
        int numGenes = parentA.getGenesPerVar() * parentA.getNumVars();
        int lowerBound = 1;
        int upperBound = numGenes - 1;
        int crossoverPoint1;
        int crossoverPoint2;
        ArrayList<BinaryIndividual> res = new ArrayList<>(2);
        ArrayList<Boolean> chromoChildA = parentA.getChromosome();
        ArrayList<Boolean> chromoChildB = parentB.getChromosome();
        
        if (reducedSurrogate) {
            for (int i = lowerBound; i < upperBound; i++) {
                if (chromoChildA.get(i) ^ chromoChildB.get(i)) {
                    lowerBound = i;
                    break;
                }
            }
            for (int i = upperBound; i > lowerBound; i--) {
                if (chromoChildA.get(i-1) ^ chromoChildB.get(i-1)) { 
                    upperBound = i;
                    break;
                }
            }
            if (lowerBound >= upperBound) { // identical chromsomes
                lowerBound = 0;
                upperBound = numGenes - 1;
            }
        }
        
        do {
            crossoverPoint1 = (int) (SGAVariationAnalysis.RAND.nextFloat() * numGenes);
        } while (crossoverPoint1 < lowerBound || crossoverPoint1 > upperBound);
        
        do {
            crossoverPoint2 = (int) (SGAVariationAnalysis.RAND.nextFloat() * numGenes);
        } while (crossoverPoint2 < lowerBound || crossoverPoint2 > upperBound || 
                crossoverPoint2 == crossoverPoint1);
        
        if (SGAVariationAnalysis.RAND.nextFloat() < SGAVariationAnalysis.CROSSOVER_PROB) {
            
            if (crossoverPoint1 > crossoverPoint2) {
                int tmp = crossoverPoint1;
                crossoverPoint1 = crossoverPoint2;
                crossoverPoint2 = tmp;
            }
            
            for (int j = crossoverPoint1; j < crossoverPoint2; j++) {
                    chromoChildA.set(j, parentB.getChromosome().get(j));
                    chromoChildB.set(j, parentA.getChromosome().get(j));
                }
        }
        
        res.add(0, new BinaryIndividual(chromoChildA, parentA.getFitFunction()));
        res.add(1, new BinaryIndividual(chromoChildB, parentB.getFitFunction()));
        
        return res;
    }
    
    /**
     * Returns a list containing the two children generated from the given
     * parents using a ring crossover method. 
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @return a list containing two children
     */
    public static ArrayList<BinaryIndividual> ringCrossover (
            BinaryIndividual parentA,
            BinaryIndividual parentB) {
        
        int numGenes = parentA.getGenesPerVar() * parentA.getNumVars();
        int lowerBound = 1;
        int upperBound = numGenes - 1;
        int cutPoint;
        
        ArrayList<BinaryIndividual> res = new ArrayList<>(2);
        ArrayList<Boolean> chromoChildA = new ArrayList<>();
        ArrayList<Boolean> chromoChildB = new ArrayList<>();
        
        ArrayList<Boolean> parentRing = new ArrayList<>();
        parentRing.addAll(parentA.getChromosome());
        parentRing.addAll(parentB.getChromosome());
        
        do {
            cutPoint = (int) (SGAVariationAnalysis.RAND.nextFloat() * numGenes);
        } while (cutPoint < lowerBound || cutPoint > upperBound);
        
        for (int i = 0, cp = cutPoint; i < numGenes; i++, cp++) {
            chromoChildA.add(i, parentRing.get(cp % parentRing.size()));
        }
        
        for (int i = 0, cp = cutPoint; i < numGenes; i++, cp--) {
            int n = parentRing.size();
            chromoChildB.add(i, parentRing.get(((cp % n) + n) % n));
        }
        
        res.add(0, new BinaryIndividual(chromoChildA, parentA.getFitFunction()));
        res.add(1, new BinaryIndividual(chromoChildB, parentB.getFitFunction()));
        
        return res;
    }
    
    
/*============================== Mutation Methods ============================*/

    
    /**
     * Performs a bit-flip mutation on the given individual.
     * 
     * @param individual the individual to be mutated
     */
    public static void bitFlipMutation(BinaryIndividual individual) {
        
        for (int i = 0; i < individual.getChromosome().size(); i++) {
            if (SGAVariationAnalysis.RAND.nextFloat() < SGAVariationAnalysis.MUTATION_PROB) {
                individual.setGene(i, !individual.getGene(i));
            }
        }
    }
}
