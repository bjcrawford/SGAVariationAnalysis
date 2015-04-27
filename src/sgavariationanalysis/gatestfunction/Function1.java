/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   Function1.java
 */
package sgavariationanalysis.gatestfunction;

import java.util.ArrayList;

/**
 * A simple sinusoidal function with many local minima and maxima. This is
 * a maximum problem with a global maxima at f(1.85) = 3.85.
 * 
 * max f(x) = x * sin(10 * pi * x) + 2.0, 
 * 
 * s.t. -1 < x < 2
 * 
 * Dimensions: 1
 * 
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class Function1 implements GATestFunction {
    
    /*
       ==================================
       | Length of gene | Maximum error |
       ==================================
       |  6             |  0.04687500   |
       |  8             |  0.01171875   |
       |  10            |  0.00292969   |
       |  12            |  0.00073242   |
       |  14            |  0.00018311   |
       |  16            |  0.00004578   |
       |  18            |  0.00001144   |
       |  20            |  0.00000286   |
       ==================================
                for -1 < x < 2
    */
    
    /**
     * Returns true if the function is a maximum problem, and false if 
     * the function is a minimum problem.
     * 
     * @return true is maximum, false if minimum
     */
    @Override
    public boolean isMaxProblem() {
        return true;
    }
    
    /**
     * Returns the numbers of variables input into the function.
     * 
     * @return the number of variables
     */
    @Override
    public int getNumVars() {
        return 1;
    }
    
    /**
     * The number of genes used to represent each variable.
     * 
     * @return the number of genes per variable
     */
    @Override
    public int getGenesPerVar() {
        return 12;
    }
    
    /**
     * The upper bound for input variables.
     * 
     * @return the x upper bound
     */
    @Override
    public int getXUpperBound() {
        return 2;
    }
    
    /**
     * The lower bound for input variables.
     * 
     * @return the x lower bound
     */
    @Override
    public int getXLowerBound() {
        return -1;
    }
    
    /**
     * Calculates and returns the fitness value for the given individual.
     * 
     * @param reals the value to calculate the fitness from
     * @return the fitness value
     */
    @Override
    public float calculateFitness(ArrayList<Float> reals) {
        
        float real = reals.get(0);
        
        return real * (float) Math.sin(10.0f * (float) Math.PI * real) + 2.0f;
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
        return fitness;
    }
        
    /**
     * Returns the optimal solutions for the problem.
     * 
     * @return the optimal solution
     */
    @Override
    public float getOptimalSolution() {
        return 3.85f;
    }
    
    @Override
    public String toString() {
        return "  Name: Function1\n" +
                "  Fitness Formula: max f(x) = x * sin(10 * pi * x) + 2.0\n" +
                "  Optimal Solution: " + getOptimalSolution();
    }
}
