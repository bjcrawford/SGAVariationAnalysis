/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   Individual.java
 */
package sgavariationanalysis;

import sgavariationanalysis.gatestfunction.GATestFunction;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A class representing an individual in a GA.
 * 
 * @author Brett Crawford <brett.crawford@temple.edu>
 */
public class BinaryIndividual {

    
/*============================== Member Variables ============================*/

    
    /**
     * The location, a list of lists of boolean values representing the genes 
     * of the chromosomes in binary format (genotype). Each boolean represents
     * a gene and each list represents a separate variable. 
     */
    private final ArrayList<ArrayList<Boolean>> chromosome;

    /**
     * The location, the real number representation the chromosome 
     * (phenotype). Each float represents a separate variable.
     */
    private ArrayList<Float> realValues;

    /**
     * The quality, the objective value, a float representing the evaluation 
     * of the chromosome according to the fitness function. Each float 
     * represents a separate variable.
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
     * A flag for indicating the use of gray code
     */
    private final boolean isGray;
    
    /**
     * The Random object to use for prn generation
     */
    private final Random rand;
    
 
/*================================ Constructors ==============================*/

    /**
     * Creates an individual with num variables of len length, in bits, and
     * randomly generates a chromosome.
     * 
     * @param testFunction the test function 
     * @param rand the pseudo-random number generator
     * @param isGray a flag for gray code representation
     */
    public BinaryIndividual(GATestFunction testFunction, Random rand,
            boolean isGray) {
        
        int num = testFunction.getNumVars();
        int len = testFunction.getGenesPerVar();
        chromosome = new ArrayList<>();
        for (int vars = 0; vars < num; vars++) {
            chromosome.add(vars, new ArrayList<>());
            for (int i = 0; i < len; i++) {
                chromosome.get(vars).add(i, Boolean.FALSE);
            }
          
        }
        this.testFunction = testFunction;
        this.rand = rand;
        this.isGray = isGray;
        generateRandomChromosome();
        updateValues();
        relFitness = 0;
    }
    
    /**
     * Creates an individual with the given chromosome. The individual's 
     * other properties are initialized using the parent values.
     * 
     * @param chromosome the chromosome
     * @param parent the parent individual to inherit properties from
     */
    public BinaryIndividual(ArrayList<Boolean> chromosome,
            BinaryIndividual parent) {
        int num = parent.getTestFunction().getNumVars();
        int len = parent.getTestFunction().getGenesPerVar();
        this.chromosome = new ArrayList<>();
        for (int vars = 0; vars < num; vars++) {
            this.chromosome.add(vars, new ArrayList<>());
            for (int i = 0; i < len; i++) {
                this.chromosome.get(vars).add(i, 
                        chromosome.get(vars * num + i) ? 
                        Boolean.TRUE : Boolean.FALSE);
            }
        }
        this.testFunction = parent.getTestFunction();
        this.rand = parent.getRand();
        this.isGray = parent.isGray();
        updateValues();
        relFitness = 0;
    }
    
    /**
     * Creates an individual as a copy of the given individual.
     * 
     * @param toCopy 
     */
    public BinaryIndividual(BinaryIndividual toCopy) {
        
        chromosome = new ArrayList<>();
        for (int vars = 0; vars < toCopy.getNumVars(); vars++) {
            chromosome.add(vars, new ArrayList<>());
            for (int i = 0; i < toCopy.getGenesPerVar(); i++) {
                chromosome.get(vars).add(i, 
                        toCopy.getChromosome()
                                .get(vars * toCopy.getNumVars() + i) ? 
                                Boolean.TRUE : Boolean.FALSE
                );
            }
        }
        this.testFunction = toCopy.getTestFunction();
        this.rand = toCopy.getRand();
        this.isGray = toCopy.isGray();
        updateValues();
        relFitness = 0;
    }
    
    
/*============================== Private Methods =============================*/
  
    /**
     * Populates the chromosome of this individual with random values.
     */
    private void generateRandomChromosome() {
        
        
        for (ArrayList<Boolean> var : chromosome) {
            for (int i = 0; i < getGenesPerVar(); i++) {
                var.set(i, rand.nextBoolean());
            }
        }
    }

    /**
     * Updates the real and objective values associated with this
     * individual's chromosome.
     */
    private void updateValues() {
        realValues = chromoToReal();
        objValue = getTestFunction().calculateFitness(this);
        fitTransValue = getTestFunction().getFitnessTransferral(objValue);
    }
    
    /**
     * Returns the real value number representation of this individual
     * with the chromosome being represented as either binary or gray code
     * according to the program flag.
     * 
     * @return The real value number
     */
    private ArrayList<Float> chromoToReal() {
        
        ArrayList<Float> res = new ArrayList<>();
        float xLower = testFunction.getXLowerBound();
        float xUpper = testFunction.getXUpperBound();
        
        for (int vars = 0; vars < getNumVars(); vars++) {
            long binary = 0;
            for (int i = 0; i < getGenesPerVar(); i++) {
                binary += chromosome.get(vars).get(i) ? Math.pow(2, i) : 0;
            }
            
            if (isGray) {
                binary = (binary >> 1) ^ binary;
            }
            
            res.add(vars, (float) binary / 
                    (float) Math.pow(2, chromosome.get(vars).size()) * 
                    (xUpper - xLower) + xLower
            );
        }
        
        return res;
    }
    
    
/*============================ Getters and Setters ===========================*/

    
    /**
     * @return the number of variables represented in the chromosome
     */
    public int getNumVars() {
        return getTestFunction().getNumVars();
    }
    
    /**
     * @return the number of genes, in bits, per variable
     */
    public int getGenesPerVar() {
        return getTestFunction().getGenesPerVar();
    }
    
    /**
     * @param locus the location of the gene
     * @return the gene
     */
    public Boolean getGene(int locus) {
        return chromosome.get(locus / (getNumVars() * getGenesPerVar()))
                .get(locus % getGenesPerVar());
    }
    
    /**
     * @param locus the location of the gene
     * @param gene the gene
     */
    public void setGene(int locus, Boolean gene) {
        
        chromosome.get(locus / (getNumVars() * getGenesPerVar()))
                .set(locus % getGenesPerVar(), gene);
        updateValues();
    }
    
    /**
     * This method returns the chromosome as a single list of booleans.
     * 
     * @return the chromosome
     */
    public ArrayList<Boolean> getChromosome() {
        
        ArrayList<Boolean> res = new ArrayList<>();
        
        for (int vars = 0; vars < getNumVars(); vars++) {
            for (int i = 0; i < getGenesPerVar(); i++) {
                res.add(vars * getNumVars() + i, 
                        chromosome.get(vars).get(i) ? 
                        Boolean.TRUE : Boolean.FALSE);
            }
        }
        
        return res;
    }

    /**
     * @return the realValue
     */
    public ArrayList<Float> getRealValue() {
        return realValues;
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
     * @return the genotype
     */
    public String getGenotype() {
        String genotype = "";
        for (ArrayList<Boolean> var : chromosome) {
            for (int i = getGenesPerVar() - 1; i >= 0; i--) {
                genotype += var.get(i) ? "1" : "0";
            }
        }
        
        return genotype;
    }

    /**
     * @return the fitFunction
     */
    public GATestFunction getTestFunction() {
        return testFunction;
    }

    /**
     * @return the isGray
     */
    public boolean isGray() {
        return isGray;
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
        res += realValues.stream()
                .map((value) -> "" + value)
                .collect(Collectors.joining(", "));
        res += "]\n  Relative Fitness: " + relFitness +
               "\n  Genotype: " + getGenotype();
        if (!testFunction.isMaxProblem()) {
            res += "\n  Fitness Transferral: " + fitTransValue;
        }
        
        return res;
    }
    
}
