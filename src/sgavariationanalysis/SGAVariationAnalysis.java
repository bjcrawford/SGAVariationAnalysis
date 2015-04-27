/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   SGAVariationAnalysis.java
 */
package sgavariationanalysis;

import sgavariationanalysis.binary.BinaryVariation;
import sgavariationanalysis.binary.BinaryPopulation;
import sgavariationanalysis.binary.BinaryIndividual;
import java.util.Random;
import java.util.Scanner;
import sgavariationanalysis.continuous.ContinuousIndividual;
import sgavariationanalysis.continuous.ContinuousPopulation;
import sgavariationanalysis.gatestfunction.GATestFunction;
import sgavariationanalysis.gatestfunction.Function1;
import sgavariationanalysis.gatestfunction.Function2;
import sgavariationanalysis.gatestfunction.Function3;


public class SGAVariationAnalysis {
    
    public static final int POP_SIZE = 20;
    public static final int MAX_GEN = 20;
    public static final float CROSSOVER_PROB = 0.8f;
    public static final float MUTATION_PROB = 0.01f;
    public static final int NUM_TRIALS = 100;
    public static final Random RAND = new Random(System.currentTimeMillis());
    public static final boolean IS_GRAY = false;

    public static void main(String[] args) {
        
        int welcomeSelect = welcomeSelection();
        GATestFunction functionSelect = functionSelection();
        
        switch (welcomeSelect) {
            case 1:
                runBinaryTrial(functionSelect, false);
                break;
            case 2:
                runBinaryTrial(functionSelect, true);
                break;
            case 3:
                runContinuousTrial(functionSelect);
                break;
            default:
                
        }
        
    }
    
    private static void runBinaryTrial(GATestFunction testFunc, boolean isGray) {
        
        BinaryPopulation pop;
        BinaryIndividual worstInd = null;
        BinaryIndividual bestInd = null;
        float totalObj = 0.0f;
        float meanObj;
        int crossoverId = binaryCrossoverSelection();
        
        for (int trial = 0; trial < NUM_TRIALS; trial++) {
         
            pop = new BinaryPopulation(testFunc, RAND, isGray, crossoverId);
            worstInd = pop.getPopulation().get(0);
            bestInd = pop.getPopulation().get(0);

            for (int gen = 0; gen < MAX_GEN; gen++) {

                for (BinaryIndividual bi : pop.getPopulation()) {
                    
                    if (testFunc.isMaxProblem()) {
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
                pop.reproduce();
            }
            
        }
        
        meanObj = totalObj / (NUM_TRIALS * MAX_GEN * POP_SIZE);
        
        System.out.println("\nTest Function:\n" + testFunc);
        System.out.println("\nBest Individual:\n" + bestInd);
        System.out.println("\nWorst Individual:\n" + worstInd);
        System.out.println("\nMean Individual:\n" + 
                "  Objective Value: " + meanObj);
        if (!testFunc.isMaxProblem()) {
            System.out.println("  Fitness transferral: " + 
                    (testFunc.getFitnessTransferral(meanObj)));
        }
    }
    
    public static void runContinuousTrial(GATestFunction testFunc) {
        
        ContinuousPopulation pop;
        ContinuousIndividual worstInd = null;
        ContinuousIndividual bestInd = null;
        float totalObj = 0.0f;
        float meanObj;
        int crossoverId = continuousCrossoverSelection();
        
        for (int trial = 0; trial < NUM_TRIALS; trial++) {
         
            pop = new ContinuousPopulation(testFunc, RAND, crossoverId);
            worstInd = pop.getPopulation().get(0);
            bestInd = pop.getPopulation().get(0);

            for (int gen = 0; gen < MAX_GEN; gen++) {

                for (ContinuousIndividual ci : pop.getPopulation()) {
                    
                    if (testFunc.isMaxProblem()) {
                        if (ci.getObjValue() > bestInd.getObjValue()) {
                            bestInd = ci;
                        }
                        else if (ci.getObjValue() < worstInd.getObjValue()) {
                            worstInd = ci;
                        }
                    }
                    else {
                        if (ci.getObjValue() < bestInd.getObjValue()) {
                            bestInd = ci;
                        }
                        else if (ci.getObjValue() > worstInd.getObjValue()) {
                            worstInd = ci;
                        }
                    }
                    totalObj += ci.getObjValue();
                }

                pop.rwSelect();
                pop.reproduce();
            }
            
        }
        
        meanObj = totalObj / (NUM_TRIALS * MAX_GEN * POP_SIZE);
        
        System.out.println("\nTest Function:\n" + testFunc);
        System.out.println("\nBest Individual:\n" + bestInd);
        System.out.println("\nWorst Individual:\n" + worstInd);
        System.out.println("\nMean Individual:\n" + 
                "  Objective Value: " + meanObj);
        if (!testFunc.isMaxProblem()) {
            System.out.println("  Fitness transferral: " + 
                    (testFunc.getFitnessTransferral(meanObj)));
        }
    }
    
    public static int welcomeSelection() {
        
        Scanner s = new Scanner(System.in);
        
        System.out.println("\n1. Binary");
        System.out.println("2. Gray");
        System.out.println("3. Continuous");
        System.out.print("\nPlease choose a chromosome representation: ");
        
        return s.nextInt();
    }
    
    public static int binaryCrossoverSelection() {
        
        Scanner s = new Scanner(System.in);
        
        System.out.println("\n1. Single Point Crossover");
        System.out.println("2. Dual Point Crossover");
        System.out.println("3. Single Point Crossover with Reduced Surrogate");
        System.out.println("4. Dual Point Crossover with Reduced Surrogate");
        System.out.println("5. Ring Crossover");
        System.out.println("6. Uniform Crossover");
        System.out.println("7. Shuffle Crossover");
        System.out.println("8. Shuffle Crossover with Reduced Surrogate");
        System.out.println("9. Three Parent Crossover");
        System.out.print("\nPlease choose a crossover method: ");
        
        return s.nextInt();
    }
    
    public static int continuousCrossoverSelection() {
        
        Scanner s = new Scanner(System.in);
        
        System.out.println("\n1. Whole Arithmetic Crossover");
        System.out.println("2. Local Arithmetic Crossover");
        System.out.println("3. Linear Crossover");
        System.out.println("4. Heuristic Crossover");
        System.out.println("5. Blend Crossover");
        System.out.print("\nPlease choose a crossover method: ");
        
        return s.nextInt();
    }
    
    public static GATestFunction functionSelection() {
        
        GATestFunction res;
        Scanner s = new Scanner(System.in);
        
        System.out.println("\n1. Function1");
        System.out.println("2. Function2");
        System.out.println("3. Function3");
        System.out.print("\nPlease choose a test function: ");
        
        switch(s.nextInt()) {
            case 1:
                res = new Function1();
                break;
            case 2:
                res = new Function2();
                break;
            case 3:
                res = new Function3();
                break;
            default:
                res = new Function1();
        }
        
        return res;
    }
    
}
