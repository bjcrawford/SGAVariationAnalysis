/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   ContinuousPopulation.java
 */
package sgavariationanalysis.continuous;

import java.util.ArrayList;
import java.util.Random;
import sgavariationanalysis.SGAVariationAnalysis;
import sgavariationanalysis.gatestfunction.GATestFunction;

/**
 * A class representing a continuous population in a GA.
 * 
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class ContinuousPopulation {
    
    
    private static final Random RAND = SGAVariationAnalysis.RAND;
        
    
/*============================== Member Variables ============================*/

    
    /* The population of individuals */
    private final ArrayList<ContinuousIndividual> population;
    
    /* The mating pools of individuals */
    private final ArrayList<ContinuousIndividual> matingPool;
    
    /* The id of the crossover variation method */
    private final int crossoverId;
    
     
/*================================ Constructors ==============================*/

    /**
     * Creates a population using the parameters specified in the given
     * fitness function.
     * 
     * @param testFunction the fitness function to use
     * @param rand the pseudo-random number generator
     * @param crossoverId
     */
    public ContinuousPopulation(GATestFunction testFunction, Random rand,
            int crossoverId) {
        
        population = new ArrayList<>();
        matingPool = new ArrayList<>();
        this.crossoverId = crossoverId;
        
        for (int i = 0; i < SGAVariationAnalysis.POP_SIZE; i++) {
            population.add(i, new ContinuousIndividual(testFunction, rand));
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
        
        for (ContinuousIndividual ci : population) {
            totalFitness += ci.getFitTransValue();
        }
        
        for (ContinuousIndividual ci : population) {
            ci.setRelFitness(ci.getFitTransValue() / totalFitness);
        }
    }
           
    
/*=============================== Public Methods =============================*/
  
    /**
     * Uses roulette wheel selection to populate the mating pool. This method
     * uses the relative fitness values to determine the size of each
     * individual's "slice" of the roulette wheel. Selection is done
     * with replacement, meaning an individual can be selected from the 
     * population into the mating pool multiple times.
     */
    public void rwSelect() {
        
        for (int i = 0; i < matingPool.size(); i++) {
            
            float select = RAND.nextFloat();
            float sliceLow = 0.0f;
            float sliceHigh;
            
            for (ContinuousIndividual ci : population) {
                sliceHigh = sliceLow + ci.getRelFitness();
                if (select >= sliceLow && select <= sliceHigh) {
                    matingPool.set(i, new ContinuousIndividual(ci));
                    break;
                }
                sliceLow = sliceHigh;
            }
        }
    }
    
    /**
     * Mates the individuals in the mating pool to create a new population
     * generation. This method mates adjacent individuals within the mating
     * pool list (i.e., 0 and 1, 2 and 3, etc...). After performing the 
     * crossover operation, each child individual undergoes the mutation 
     * operation.
     */
    public void reproduce() {
        
        population.stream().forEach((ci) -> {
            ci = null;
        });
        
        for (int i = 0; i < matingPool.size(); i += 2) {
            
            ContinuousIndividual parentA = matingPool.get(i);
            ContinuousIndividual parentB = matingPool.get(i + 1);
            ArrayList<ContinuousIndividual> children;
            
            switch(crossoverId) {
                case ContinuousVariation.WAC:
                     children = ContinuousVariation
                             .arithmeticCrossover(parentA, parentB, false);
                    break;
                case ContinuousVariation.LAC:
                    children = ContinuousVariation
                            .arithmeticCrossover(parentA, parentB, true);
                    break;
                case ContinuousVariation.LC:
                    children = ContinuousVariation
                            .linearCrossover(parentA, parentB);
                    break;
                case ContinuousVariation.HC:
                    children = ContinuousVariation
                            .heuristicCrossover(parentA, parentB);
                    break;
                case ContinuousVariation.BC:
                    children = ContinuousVariation
                            .blendCrossover(parentA, parentB);
                    break;
                default:
                    System.out.println("ContinuousPopulation: Invalid id. "
                            + "Using WAC.");
                    children = ContinuousVariation
                            .arithmeticCrossover(parentA, parentB, false);
            }
            
            ContinuousVariation.uniformMutation(children.get(0));
            ContinuousVariation.uniformMutation(children.get(1));
            
            population.set(i, children.get(0));
            population.set(i + 1, children.get(1));
        }
        
        matingPool.stream().forEach((ci) -> {
            ci = null;
        });
        
        calcRelFitness();
    }
    
    
/*============================ Getters and Setters ===========================*/

    /**
     * @return the population
     */
    public ArrayList<ContinuousIndividual> getPopulation() {
        
        return population;
    }
    
    /**
     * @return the mating pool
     */
    public ArrayList<ContinuousIndividual> getMatingPool() {
        
        return matingPool;
    }
}
