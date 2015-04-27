/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   ContinuousIndividual.java
 */
package sgavariationanalysis.continuous;

import sgavariationanalysis.gatestfunction.GATestFunction;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A class representing a continuous individual in a GA.
 * 
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class ContinuousIndividual {

    
/*============================== Member Variables ============================*/

    
    /**
     * The location, a list of continuous values representing the genes 
     * of the chromosomes (genotype). Each float represents a separate 
     * variable. 
     */
    private final ArrayList<Float> chromosome;

    /**
     * The quality, the objective value, a float representing the evaluation 
     * of the chromosome according to the fitness function.
     */
    private float objValue;
    
    /**
     * In the case of minimization problems, a transferral is needed to map
     * the objective value to a value that can be used to calculate relative
     * fitness.
     */
    private float fitTransValue;

    /**
     * The relative fitness of the individual, the fitness (objective value) 
     * divided by the the sum of the population's fitness.
     */
    private float relFitness;
    
    /**
     * The test function to use for the individual.
     */
    private final GATestFunction testFunction;
    
    /**
     * The Random object to use for prn generation
     */
    private final Random rand;
    
 
/*================================ Constructors ==============================*/

    /**
     * Creates an individual with num variables and randomly generates a 
     * chromosome.
     * 
     * @param testFunction the test function 
     * @param rand the pseudo-random number generator
     */
    public ContinuousIndividual(GATestFunction testFunction, Random rand) {
        
        int num = testFunction.getNumVars();
        int len = testFunction.getGenesPerVar();
        chromosome = new ArrayList<>();
        for (int vars = 0; vars < num; vars++) {
            chromosome.add(vars, 0.0f);
        }
        this.testFunction = testFunction;
        this.rand = rand;
        generateRandomChromosome();
        updateValues();
        relFitness = 0.0f;
    }
    
    /**
     * Creates an individual with the given chromosome. The individual's 
     * other properties are initialized using the parent values.
     * 
     * @param chromosome the chromosome
     * @param parent the parent individual to inherit properties from
     */
    public ContinuousIndividual(ArrayList<Float> chromosome,
            ContinuousIndividual parent) {
        int num = parent.getTestFunction().getNumVars();
        int len = parent.getTestFunction().getGenesPerVar();
        this.chromosome = new ArrayList<>();
        for (int vars = 0; vars < num; vars++) {
            this.chromosome.add(vars, chromosome.get(vars));
        }
        this.testFunction = parent.getTestFunction();
        this.rand = parent.getRand();
        updateValues();
        relFitness = 0;
    }
    
    /**
     * Creates an individual as a copy of the given individual.
     * 
     * @param toCopy 
     */
    public ContinuousIndividual(ContinuousIndividual toCopy) {
        
        chromosome = new ArrayList<>();
        chromosome.addAll(toCopy.getChromosome());
        this.testFunction = toCopy.getTestFunction();
        this.rand = toCopy.getRand();
        updateValues();
        relFitness = 0;
    }
    
    
/*============================== Private Methods =============================*/
  
    /**
     * Populates the chromosome of this individual with random values.
     */
    private void generateRandomChromosome() {
        
        
        for (int i = 0; i < chromosome.size(); i++) {
            
            int l = testFunction.getXLowerBound();
            int u = testFunction.getXUpperBound();
            
            chromosome.set(i, rand.nextFloat() * (u - l) + l);
        }
    }

    /**
     * Updates the real and objective values associated with this
     * individual's chromosome.
     */
    private void updateValues() {
        objValue = getTestFunction().calculateFitness(chromosome);
        fitTransValue = getTestFunction().getFitnessTransferral(objValue);
    }
    
    
/*============================ Getters and Setters ===========================*/

    
    /**
     * @return the number of variables represented in the chromosome
     */
    public int getNumVars() {
        return getTestFunction().getNumVars();
    }
    
    /**
     * 
     * @param index
     * @param var 
     */
    public void setVar(int index, float var) {
        chromosome.set(index, var);
    }
    
    /**
     * This method returns the chromosome as a single list of floats.
     * 
     * @return the chromosome
     */
    public ArrayList<Float> getChromosome() {
        
        ArrayList<Float> res = new ArrayList<>();
        
        for (int vars = 0; vars < getNumVars(); vars++) {
            res.add(vars, chromosome.get(vars));
        }
        
        return res;
    }

    /**
     * @return the objValue
     */
    public float getObjValue() {
        return objValue;
    }

    /**
     * @return the fitTransValue
     */
    public float getFitTransValue() {
        return fitTransValue;
    }

    /**
     * @param fitTransValue the fitTransValue to set
     */
    public void setFitTransValue(float fitTransValue) {
        this.fitTransValue = fitTransValue;
    }

    /**
     * @return the relFitness
     */
    public float getRelFitness() {
        return relFitness;
    }

    /**
     * @param relFitness the relFitness to set
     */
    public void setRelFitness(float relFitness) {
        this.relFitness = relFitness;
    }

    /**
     * @return the fitFunction
     */
    public GATestFunction getTestFunction() {
        return testFunction;
    }

    /**
     * @return the rand
     */
    public Random getRand() {
        return rand;
    }
    
    @Override
    public String toString() {
        
        String res = "";
        
        res += "  Objective Value: " + objValue;
        res += "\n  Real Values: [";
        res += chromosome.stream()
                .map((value) -> "" + value)
                .collect(Collectors.joining(", "));
        res += "]\n  Relative Fitness: " + relFitness;
        if (!testFunction.isMaxProblem()) {
            res += "\n  Fitness Transferral: " + fitTransValue;
        }
        
        return res;
    }
    
}
