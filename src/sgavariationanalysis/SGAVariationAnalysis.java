/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   SGAVariationAnalysis.java
 */
package sgavariationanalysis;

import java.util.Random;
import sgavariationanalysis.gatestfunction.GATestFunction;
import sgavariationanalysis.gatestfunction.Function1;
import sgavariationanalysis.gatestfunction.Function2;
import sgavariationanalysis.gatestfunction.Function3;


public class SGAVariationAnalysis {
    
    public static final int POP_SIZE = 20;
    public static final int MAX_GEN = 20;
    public static final float CROSSOVER_PROB = 0.8f;
    public static final float MUTATION_PROB = 0.01f;
    public static final int NUM_TRIALS = 30;
    public static final boolean IS_GRAY = false;
    
    public static final Random RAND = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        
        GATestFunction fitFunc = new Function3();
        BinaryPopulation pop;
        BinaryIndividual worstInd = null;
        BinaryIndividual bestInd = null;
        float totalObj = 0.0f;
        float meanObj;
        
        for (int trial = 0; trial < NUM_TRIALS; trial++) {
         
            pop = new BinaryPopulation(fitFunc);
            worstInd = pop.getPopulation().get(0);
            bestInd = pop.getPopulation().get(0);

            for (int gen = 0; gen < MAX_GEN; gen++) {

                for (BinaryIndividual bi : pop.getPopulation()) {
                    
                    if (fitFunc.isMaxProblem()) {
                        if (bi.getObjValue() > bestInd.getObjValue()) {
                            bestInd = bi;
                        }
                        else if (bi.getObjValue() < worstInd.getObjValue()) {
                            worstInd = bi;
                        }
                    }
                    else {
                        if (bi.getObjValue() < bestInd.getObjValue()) {
                            bestInd = bi;
                        }
                        else if (bi.getObjValue() > worstInd.getObjValue()) {
                            worstInd = bi;
                        }
                    }
                    totalObj += bi.getObjValue();
                }

                pop.rwSelect();
                pop.reproduce(BinaryVariation.TPC);
            }
            
        }
        
        meanObj = totalObj / (NUM_TRIALS * MAX_GEN * POP_SIZE);
        
        System.out.println("Optimal Solution: " + fitFunc.getOptimalSolution());
        
        System.out.println("\nBest Individual:\n" + bestInd);
        
        System.out.println("\nWorst Individual:\n" + worstInd);
        
        System.out.println("\nMean Individual:\n" + 
                "  Objective Value: " + meanObj);
        if (!fitFunc.isMaxProblem()) {
            System.out.println("  Fitness transferral: " + (fitFunc.getFitnessTransferral(meanObj)));
        }
        
    }
    
}
