/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   BinaryPopulation.java
 */
package sgavariationanalysis.binary;

import java.util.ArrayList;
import java.util.Random;
import sgavariationanalysis.SGAVariationAnalysis;
import sgavariationanalysis.gatestfunction.GATestFunction;

/**
 * A class representing a binary population in a GA.
 * 
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class BinaryPopulation {
    
    
    private static final Random RAND = SGAVariationAnalysis.RAND;
        
    
/*============================== Member Variables ============================*/

    
    /* The population of individuals */
    private final ArrayList<BinaryIndividual> population;
    
    /* The mating pools of individuals */
    private final ArrayList<BinaryIndividual> matingPool;
    
    /* The id of the crossover variation method */
    private final int crossoverId;
    
     
/*================================ Constructors ==============================*/

    /**
     * Creates a population using the parameters specified in the given
     * fitness function.
     * 
     * @param testFunction the fitness function to use
     * @param rand the pseudo-random number generator
     * @param isGray a flag for gray code representation
     * @param crossoverId
     */
    public BinaryPopulation(GATestFunction testFunction, Random rand,
            boolean isGray, int crossoverId) {
        
        population = new ArrayList<>();
        matingPool = new ArrayList<>();
        this.crossoverId = crossoverId;
        
        for (int i = 0; i < SGAVariationAnalysis.POP_SIZE; i++) {
            population.add(i, new BinaryIndividual(testFunction, rand, isGray));
        }
        
        for (int i = 0; i < SGAVariationAnalysis.POP_SIZE; i++) {
            matingPool.add(i, null);
        }
        
        calcRelFitness();
    }
        
    
/*============================== Private Methods =============================*/
  
    /**
     * Calculates the relative fitness of each individual in the population.
     * If the fitness function reports maximum problem, the objective (fitness)
     * value is used in the calculations. If the fitness function reports a 
     * minimum problem, the fitness transferral value is used in the
     * calculation.
     */
    private void calcRelFitness() {
        
        float totalFitness = 0;
        
        for (BinaryIndividual bi : population) {
            
            if (bi.getTestFunction().isMaxProblem()) {
                totalFitness += bi.getObjValue();
            }
            else {
                totalFitness += bi.getFitTransValue();
            }
        }
        
        for (BinaryIndividual bi : population) {
            
            if (bi.getTestFunction().isMaxProblem()) {
                bi.setRelFitness(bi.getObjValue() / totalFitness);
            }
            else {
                bi.setRelFitness(bi.getFitTransValue() / totalFitness);
            }
        }
    }
           
    
/*=============================== Public Methods =============================*/
  
    /**
     * Uses roulette wheel selection to populate the mating pool. This method
     * uses the relative fitness values to determine the size of each
     * individual's "slice" of the roulette wheel. Selection is done with
     * with replacement, meaning an individual can be selected from the 
     * population into the mating pool multiple times.
     */
    public void rwSelect() {
        
        for (int i = 0; i < matingPool.size(); i++) {
            
            float select = RAND.nextFloat();
            float sliceLow = 0.0f;
            float sliceHigh;
            
            for (BinaryIndividual bi : population) {
                sliceHigh = sliceLow + bi.getRelFitness();
                if (select >= sliceLow && select <= sliceHigh) {
                    matingPool.set(i, new BinaryIndividual(bi));
                    break;
                }
                sliceLow = sliceHigh;
            }
        }
    }
    
    /**
     * Mates the individuals in the mating pool to create a new population
     * generation. This method mates adjacent individuals within the mating
     * pool list (i.e., 0 and 1, 2 and 3, etc..). After performing the 
     * crossover operation, each child individual undergoes the mutation 
     * operation.
     */
    public void reproduce() {
        
        population.stream().forEach((bi) -> {
            bi = null;
        });
        
        for (int i = 0; i < matingPool.size(); i += 2) {
            
            BinaryIndividual parentA = matingPool.get(i);
            BinaryIndividual parentB = matingPool.get(i + 1);
            ArrayList<BinaryIndividual> children;
            
            switch(crossoverId) {
                case BinaryVariation.SPC:
                     children = BinaryVariation
                             .singlePointCrossover(parentA, parentB, false);
                    break;
                case BinaryVariation.DPC:
                    children = BinaryVariation
                            .dualPointCrossover(parentA, parentB, false);
                    break;
                case BinaryVariation.SPCRS:
                     children = BinaryVariation
                             .singlePointCrossover(parentA, parentB, true);
                    break;
                case BinaryVariation.DPCRS:
                    children = BinaryVariation
                            .dualPointCrossover(parentA, parentB, true);
                    break;
                case BinaryVariation.RC:
                    children = BinaryVariation
                            .ringCrossover(parentA, parentB);
                    break;
                case BinaryVariation.UC:
                    children = BinaryVariation
                            .uniformCrossover(parentA, parentB);
                    break;
                case BinaryVariation.SC:
                    children = BinaryVariation
                            .shuffleCrossover(parentA, parentB, false);
                    break;
                case BinaryVariation.SCRS:
                    children = BinaryVariation
                            .shuffleCrossover(parentA, parentB, true);
                    break;
                case BinaryVariation.TPC:
                    // In this case, a third parent is chosen from the 
                    // individual remaining in the mating pool.
                    int j;
                    do {
                        j = (int) (RAND.nextFloat() * matingPool.size());
                    } while (j == i || j == i+1);
                    BinaryIndividual parentC = matingPool.get(j);
                    children = BinaryVariation
                            .threeParentCrossover(parentA, parentC, parentB);
                    break;
                default:
                    System.out.println("BinaryPopulation: Invalid id. "
                            + "Using SPC.");
                    children = BinaryVariation
                            .singlePointCrossover(parentA, parentB, false);
            }
            
            BinaryVariation.bitFlipMutation(children.get(0));
            BinaryVariation.bitFlipMutation(children.get(1));
            
            population.set(i, children.get(0));
            population.set(i + 1, children.get(1));
        }
        
        matingPool.stream().forEach((bi) -> {
            bi = null;
        });
        
        calcRelFitness();
    }
    
    
/*============================ Getters and Setters ===========================*/

    /**
     * @return the population
     */
    public ArrayList<BinaryIndividual> getPopulation() {
        
        return population;
    }
    
    /**
     * @return the mating pool
     */
    public ArrayList<BinaryIndividual> getMatingPool() {
        
        return matingPool;
    }
}
