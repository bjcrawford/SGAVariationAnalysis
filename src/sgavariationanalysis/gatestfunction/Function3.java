/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   Function3.java
 */
package sgavariationanalysis.gatestfunction;

import java.util.ArrayList;

/**
 * The Ackley function offers many local minima on the outer edges
 * with a deep global minima located at the center. This is a minimum 
 * problem with a global minimum at f(0) = 0.0.
 * 
 * min f(x) = -20 * exp(-0.2 * sqrt(1/n * sum(xi^2)) - 
 *            exp(1/n * sum(cos(2 * pi * xi))) + 20 + e, 
 * 
 * s.t. -20 < x < 30
 * 
 * Dimensions: 2
 * 
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class Function3 implements GATestFunction {

    /*
       ==================================
       | Length of gene | Maximum error |
       ==================================
       |  6             |  0.78125000   |
       |  8             |  0.19531250   |
       |  10            |  0.04882812   |
       |  12            |  0.01220703   |
       |  14            |  0.00305176   |
       |  16            |  0.00076293   |
       |  18            |  0.00019074   |
       |  20            |  0.00004768   |
       ==================================
                for -20 < x < 30
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
        return 2;
    }

    /**
     * The number of genes used to represent each variable.
     * 
     * @return the number of genes per variable
     */
    @Override
    public int getGenesPerVar() {
        return 16;
    }

    /**
     * The upper bound for input variables.
     * 
     * @return the x upper bound
     */
    @Override
    public int getXUpperBound() {
        return 30;
    }

    /**
     * The lower bound for input variables.
     * 
     * @return the x lower bound
     */
    @Override
    public int getXLowerBound() {
        return -20;
    }

    /**
     * Calculates and returns the fitness value for the given individual.
     * 
     * @param reals the value to calculate the fitness from
     * @return the fitness value
     */
    @Override
    public float calculateFitness(ArrayList<Float> reals) {
        
        float firstSum = 0.0f;
        for (float real : reals) {
            firstSum += (float) Math.pow(real, 2);
        }
        
        float firstTerm = -20.0f * (float) Math.exp(
                -0.2f * (float) Math.sqrt(firstSum / (float) getNumVars()));
        
        float secondSum = 0.0f;
        for (float real : reals) {
            secondSum += (float) Math.cos(2 * Math.PI * real);
        }
        
        float secondTerm = (float) Math.exp(secondSum / (float) getNumVars());
        
        
        
        return firstTerm - secondTerm + 20.f + (float) Math.E;
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
           transferral within the search space, but doesn't
           represent the global maxima.
        */
        float c = 25.0f;
        
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
        return "  Name: Function3\n" +
                "  Fitness Formula: min f(x) = -20 * exp(-0.2 * sqrt(1/n * sum(xi^2)) - \n" +
                "                              exp(1/n * sum(cos(2 * pi * xi))) + 20 + e" +
                "  Optimal Solution: " + getOptimalSolution();
    }
    
}
