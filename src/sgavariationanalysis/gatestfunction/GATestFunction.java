/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   FitnessFunction.java
 */
package sgavariationanalysis.gatestfunction;

import java.util.ArrayList;

/**
 * An interface for defining a testing function for a GA.
 * 
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public interface GATestFunction {
    
    /**
     * Returns true if the function is a maximum problem, and false if 
     * the function is a minimum problem.
     * 
     * @return true is maximum, false if minimum
     */
    public boolean isMaxProblem();

    /**
     * Returns the numbers of variables input into the function.
     * 
     * @return the number of variables
     */
    public int getNumVars();
    
    /**
     * The number of genes used to represent each variable.
     * 
     * @return the number of genes per variable
     */
    public int getGenesPerVar();
    
    /**
     * The upper bound for input variables.
     * 
     * @return the x upper bound
     */
    public int getXUpperBound();
    
    /**
     * The lower bound for input variables.
     * 
     * @return the x lower bound
     */
    public int getXLowerBound();
    
    /**
     * Calculates and returns the fitness value for the given individual.
     * 
     * @param reals the value(s) to calculate the fitness from
     * @return the fitness value
     */
    public float calculateFitness(ArrayList<Float> reals);
    
    /**
     * Calculate the fitness transferral value for the given fitness value.
     * For use in minimum problems. The fitness transferral value is equal
     * to f(x)max - fitness. For maximum problems this function should 
     * return the given fitness value.
     * 
     * @param fitness the unaltered fitness value for a minimum problem
     * @return the transferral fitness value
     */
    public float getFitnessTransferral(float fitness);
    
    /**
     * Returns the optimal solutions for the problem.
     * 
     * @return the optimal solution
     */
    public float getOptimalSolution();
    
}
