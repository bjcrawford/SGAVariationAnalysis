/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   VariationMethods.java
 */
package sgavariationanalysis.binary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import sgavariationanalysis.SGAVariationAnalysis;

/**
 *
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class BinaryVariation {
    
    
    private static final Random RAND = SGAVariationAnalysis.RAND;
    private static final float CROSSOVER_PROB = 
            SGAVariationAnalysis.CROSSOVER_PROB;
    private static final float MUTATION_PROB = 
            SGAVariationAnalysis.MUTATION_PROB;
    
    
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
    
    /* An id for use with the uniform crossover */
    public static final int UC = 6;
    
    /* An id for use with the shuffle crossover */
    public static final int SC = 7;
    
    /* An id for use with the shuffle crossover with reduced surrogate */
    public static final int SCRS = 8;
    
    /* An id for use with the three parent crossover */
    public static final int TPC = 9;
    
    
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
        
        ArrayList<BinaryIndividual> res = new ArrayList<>(2);
        ArrayList<Boolean> chromoChildA = parentA.getChromosome();
        ArrayList<Boolean> chromoChildB = parentB.getChromosome();
        
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
            
            int numGenes = chromoChildA.size();
            int lowerBound = 1;
            int upperBound = numGenes - 1;
            int crossPoint;

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
                crossPoint = (int) (RAND.nextFloat() * numGenes);
            } while (crossPoint < lowerBound || crossPoint > upperBound);


            for (int j = 0; j < numGenes; j++) {
                if (j > crossPoint) {
                    chromoChildA.set(j, parentB.getChromosome().get(j));
                    chromoChildB.set(j, parentA.getChromosome().get(j));
                }
            }
        }
        
        res.add(0, new BinaryIndividual(chromoChildA, parentA));
        res.add(1, new BinaryIndividual(chromoChildB, parentB));
        
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
        
        ArrayList<BinaryIndividual> res = new ArrayList<>(2);
        ArrayList<Boolean> chromoChildA = parentA.getChromosome();
        ArrayList<Boolean> chromoChildB = parentB.getChromosome();
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
            
            int numGenes = chromoChildA.size();
            int lowerBound = 1;
            int upperBound = numGenes - 1;
            int crossPoint1;
            int crossPoint2;

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
                crossPoint1 = (int) (RAND.nextFloat() * numGenes);
            } while (crossPoint1 < lowerBound || crossPoint1 > upperBound);

            do {
                crossPoint2 = (int) (RAND.nextFloat() * numGenes);
            } while (crossPoint2 < lowerBound || crossPoint2 > upperBound || 
                    crossPoint2 == crossPoint1);
        
            if (crossPoint1 > crossPoint2) {
                int tmp = crossPoint1;
                crossPoint1 = crossPoint2;
                crossPoint2 = tmp;
            }
            
            for (int j = crossPoint1; j < crossPoint2; j++) {
                    chromoChildA.set(j, parentB.getChromosome().get(j));
                    chromoChildB.set(j, parentA.getChromosome().get(j));
                }
        }
        
        res.add(0, new BinaryIndividual(chromoChildA, parentA));
        res.add(1, new BinaryIndividual(chromoChildB, parentB));
        
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
        
        ArrayList<BinaryIndividual> res = new ArrayList<>(2);
        ArrayList<Boolean> chromoChildA = parentA.getChromosome();
        ArrayList<Boolean> chromoChildB = parentB.getChromosome();
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
           
            int numGenes = chromoChildA.size();
            int lowerBound = 1;
            int upperBound = numGenes - 1;
            int cutPoint;

            ArrayList<Boolean> parentRing = new ArrayList<>();
            parentRing.addAll(parentA.getChromosome());
            parentRing.addAll(parentB.getChromosome());

            do {
                cutPoint = (int) (RAND.nextFloat() * numGenes);
            } while (cutPoint < lowerBound || cutPoint > upperBound);

            for (int i = 0, cp = cutPoint; i < numGenes; i++, cp++) {
                chromoChildA.set(i, parentRing.get(cp % parentRing.size()));
            }

            for (int i = 0, cp = cutPoint; i < numGenes; i++, cp--) {
                int n = parentRing.size();
                chromoChildB.set(i, parentRing.get(((cp % n) + n) % n));
            }
        }
        
        res.add(0, new BinaryIndividual(chromoChildA, parentA));
        res.add(1, new BinaryIndividual(chromoChildB, parentB));
        
        return res;
    }
    
    /**
     * Returns a list containing the two children generated from the given
     * parents using a uniform crossover method. 
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @return a list containing two children
     */
    public static ArrayList<BinaryIndividual> uniformCrossover (
            BinaryIndividual parentA,
            BinaryIndividual parentB) {
        
        ArrayList<BinaryIndividual> res = new ArrayList<>(2);
        ArrayList<Boolean> chromoChildA = parentA.getChromosome();
        ArrayList<Boolean> chromoChildB = parentB.getChromosome();
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
            
            for (int i = 0; i < chromoChildA.size(); i++) {
                if (RAND.nextFloat() > 0.5f) {
                    chromoChildA.set(i, parentB.getChromosome().get(i));
                    chromoChildB.set(i, parentA.getChromosome().get(i));
                }
            }
        }
        
        res.add(0, new BinaryIndividual(chromoChildA, parentA));
        res.add(1, new BinaryIndividual(chromoChildB, parentB));
        
        return res;
        
    }
    
    /**
     * Returns a list containing the two children generated from the given
     * parents using a shuffle crossover method. This method can be
     * made to use a reduced surrogate optimization by passing the the
     * flag as true.
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @param reducedSurrogate a flag for reduced surrogate
     * @return a list containing two children
     */
    public static ArrayList<BinaryIndividual> shuffleCrossover (
            BinaryIndividual parentA,
            BinaryIndividual parentB,
            boolean reducedSurrogate) {
        
        ArrayList<BinaryIndividual> res = new ArrayList<>(2);
        ArrayList<Boolean> chromoChildA = parentA.getChromosome();
        ArrayList<Boolean> chromoChildB = parentB.getChromosome();
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
         
            int numGenes = chromoChildA.size();
            int lowerBound = 1;
            int upperBound = numGenes - 1;
            int crossPoint;
        
            ArrayList<Integer> shuffleMap = new ArrayList<>();
            for (int i = 0; i < chromoChildA.size(); i++) {
                shuffleMap.add(i, i);
            }
            Collections.shuffle(shuffleMap);
            
            if (reducedSurrogate) {
                for (int i = lowerBound; i < upperBound; i++) {
                    int mi = shuffleMap.get(i);
                    if (chromoChildA.get(mi) ^ chromoChildB.get(mi)) {
                        lowerBound = i;
                        break;
                    }
                }
                for (int i = upperBound; i > lowerBound; i--) {
                    int mi = shuffleMap.get(i-1);
                    if (chromoChildA.get(mi) ^ chromoChildB.get(mi)) { 
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
                crossPoint = (int) (RAND.nextFloat() * numGenes);
            } while (crossPoint < lowerBound || crossPoint > upperBound);


            for (int i = 0; i < numGenes; i++) {
                if (i > crossPoint) {
                    int mi = shuffleMap.get(i);
                    chromoChildA.set(mi, parentB.getChromosome().get(mi));
                    chromoChildB.set(mi, parentA.getChromosome().get(mi));
                }
            }
        }
        
        res.add(0, new BinaryIndividual(chromoChildA, parentA));
        res.add(1, new BinaryIndividual(chromoChildB, parentB));
        
        return res;
        
    }
    
    /**
     * Returns a list containing the two children generated from the given
     * parents using a three parent crossover method. If a crossover is not 
     * made, parents A and C are passed back as children. Child A and B are
     * generated as follows:
     * 
     *  cA.gene(i) = !(pA.gene(i) ^ pB.gene(i)) ? pA.gene(i) : pC.gene(i)
     * 
     *  cB.gene(i) = !(pC.gene(i) ^ pB.gene(i)) ? pC.gene(i) : pA.gene(i)
     * 
     * @param parentA the first parent
     * @param parentB the second parent
     * @param parentC the third parent
     * @return a list containing two children
     */
    public static ArrayList<BinaryIndividual> threeParentCrossover (
            BinaryIndividual parentA,
            BinaryIndividual parentB,
            BinaryIndividual parentC) {
        
        ArrayList<BinaryIndividual> res = new ArrayList<>(2);
        ArrayList<Boolean> chromoChildA = parentA.getChromosome();
        ArrayList<Boolean> chromoChildB = parentC.getChromosome();
        
        if (RAND.nextFloat() < CROSSOVER_PROB) {
            
            for (int i = 0; i < chromoChildA.size(); i++) {
                
                boolean pA = parentA.getChromosome().get(i);
                boolean pB = parentB.getChromosome().get(i);
                boolean pC = parentC.getChromosome().get(i);
                
                boolean cA = !(pA ^ pB) ? pA : pC;
                boolean cB = !(pC ^ pB) ? pC : pA;
                
                chromoChildA.set(i, cA);
                chromoChildB.set(i, cB);
            }
        }
        
        res.add(0, new BinaryIndividual(chromoChildA, parentA));
        res.add(1, new BinaryIndividual(chromoChildB, parentB));
        
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
            if (RAND.nextFloat() < MUTATION_PROB) {
                individual.setGene(i, !individual.getGene(i));
            }
        }
    }
}
