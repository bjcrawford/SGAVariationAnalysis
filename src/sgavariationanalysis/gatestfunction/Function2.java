/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   Function2.java
 */
package sgavariationanalysis.gatestfunction;

import java.util.ArrayList;
import sgavariationanalysis.binary.BinaryIndividual;

/**
 * The sphere model function (De Jong's Function 1) is convex, continuous,
 * and unimodal. This is a minimum problem with a global minimum at 
 * f(0) = 0.0.
 * 
 * min f(x) = sum(xi^2), 
 * 
 * s.t. -5 < x < 5
 * 
 * Dimensions: 5
 * 
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class Function2 implements GATestFunction {

    /*
       ==================================
       | Length of gene | Maximum error |
       ==================================
       |  6             |  0.15325000   |
       |  8             |  0.03906250   |
       |  10            |  0.00976563   |
       |  12            |  0.00244141   |
       |  14            |  0.00061035   |
       |  16            |  0.00015259   |
       |  18            |  0.00003815   |
       |  20            |  0.00000954   |
       ==================================
                for -5 < x < 5
    */
    
    
    /**
     * Returns true if the function is a maximum problem, and false if 
     * the function is a minimum problem.
     * 
     * @return true is maximum, false if minimum
     */
    @Override
    public boolean isMaxProblem() {
        return false;
    }
    
    /**
     * Returns the numbers of variables input into the function.
     * 
     * @return the number of variables
     */
    @Override
    public int getNumVars() {
        return 5;
    }

    /**
     * The number of genes used to represent each variable.
     * 
     * @return the number of genes per variable
     */
    @Override
    public int getGenesPerVar() {
        return 14;
    }

    /**
     * The upper bound for input variables.
     * 
     * @return the x upper bound
     */
    @Override
    public int getXUpperBound() {
        return 5;
    }

    /**
     * The lower bound for input variables.
     * 
     * @return the x lower bound
     */
    @Override
    public int getXLowerBound() {
        return -5;
    }

    /**
     * Calculates and returns the fitness value for the given individual.
     * 
     * @param reals the value to calculate the fitness from
     * @return the fitness value
     */
    @Override
    public float calculateFitness(ArrayList<Float> reals) {
        
        float res = 0.0f;
        
        for (float real : reals) {
            res += (float) Math.pow(real, 2);
        }
        
        return res;
    }
    
    /**
     * Calculate the fitness transferral value for the given fitness value.
     * For use in minimum problems. The fitness transferral value is equal
     * to f(x)max - fitness. For maximum problems this function should 
     * return the given fitness value.
     * 
     * @param fitness the unaltered fitness value for a minimum problem
     * @return the transferral fitness value
     */
    @Override
    public float getFitnessTransferral(float fitness) {
        
        /* This should be sufficient to work for the fitness
           transferral within the search space.
        */
        float c = getNumVars() * (float) Math.pow(getXUpperBound(), 2);
        
        return c - fitness;
    }
        
    /**
     * Returns the optimal solutions for the problem.
     * 
     * @return the optimal solution
     */
    @Override
    public float getOptimalSolution() {
        return 0.0f;
    }
    
    @Override
    public String toString() {
        return "  Name: Function2\n" +
                "  Fitness Formula: min f(x) = sum(xi^2)\n" +
                "  Optimal Solution: " + getOptimalSolution();
    }
}
