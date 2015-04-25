/*
 *  Author: Brett Crawford <brett.crawford@temple.edu>
 *  File:   Individual.java
 */
package sgavariationanalysis;

import sgavariationanalysis.gatestfunction.GATestFunction;
import java.util.ArrayList;
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
    private ArrayList<ArrayList<Boolean>> chromosome;

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
    
    private final GATestFunction fitFunction;
    
 
/*================================ Constructors ==============================*/

    /**
     * Creates an individual with num variables of len length, in bits, and
     * randomly generates a chromosome.
     * 
     * @param fitFunction the fitness function 
     */
    public BinaryIndividual(GATestFunction fitFunction) {
        
        int num = fitFunction.getNumVars();
        int len = fitFunction.getGenesPerVar();
        chromosome = new ArrayList<>();
        for (int vars = 0; vars < num; vars++) {
            chromosome.add(vars, new ArrayList<>());
            for (int i = 0; i < len; i++) {
                chromosome.get(vars).add(i, Boolean.FALSE);
            }
          
        }
        this.fitFunction = fitFunction;
        generateRandomChromosome();
        updateValues();
        relFitness = 0;
    }
    
    /**
     * Creates an individual with the given chromosome.
     * 
     * @param chromosome 
     * @param fitFunction 
     */
    public BinaryIndividual(ArrayList<Boolean> chromosome,
            GATestFunction fitFunction) {
        int num = fitFunction.getNumVars();
        int len = fitFunction.getGenesPerVar();
        this.chromosome = new ArrayList<>();
        for (int vars = 0; vars < num; vars++) {
            this.chromosome.add(vars, new ArrayList<>());
            for (int i = 0; i < len; i++) {
                this.chromosome.get(vars).add(i, 
                        chromosome.get(vars * num + i) ? 
                        Boolean.TRUE : Boolean.FALSE);
            }
        }
        this.fitFunction = fitFunction;
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
        this.fitFunction = toCopy.getFitFunction();
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
                var.set(i, SGAVariationAnalysis.RAND.nextBoolean());
            }
        }
    }

    /**
     * Updates the real and objective values associated with this
     * individual's chromosome.
     */
    private void updateValues() {
        realValues = chromoToReal();
        objValue = getFitFunction().calculateFitness(this);
        fitTransValue = getFitFunction().getFitnessTransferral(objValue);
    }
    
    /**
     * Returns the real value number representation of this individual's
     * chromosome.
     * 
     * @return The real value number
     */
    private ArrayList<Float> chromoToReal() {
        
        ArrayList<Float> res = new ArrayList<>();
        float xLower = fitFunction.getXLowerBound();
        float xUpper = fitFunction.getXUpperBound();
        
        for (int vars = 0; vars < getNumVars(); vars++) {
            float real = 0.0f;
            for (int i = 0; i < getGenesPerVar(); i++) {
                real += chromosome.get(vars).get(i) ? (float) Math.pow(2, i) : 0;
            }
            
            res.add(vars, real / 
                    (float) Math.pow(2, chromosome.get(vars).size()) * 
                    (xUpper - xLower) + xLower
            );
        }
        
        return res;
    }
    
    /**
     * Returns the chromosome representation of this individual's
     * chromosome, a list of booleans.
     * 
     * @return the chromosome representation
     */
    private ArrayList<ArrayList<Boolean>> realToChromo() {
        
        ArrayList<ArrayList<Boolean>> chromo = new ArrayList<>();
        float xLower = fitFunction.getXLowerBound();
        float xUpper = fitFunction.getXUpperBound();
        
        for (int vars = 0; vars < getNumVars(); vars++) {
            chromo.add(vars, new ArrayList<>());
            float rep = (realValues.get(vars) - xLower) / (xUpper - xLower) * 
                    (float) Math.pow(2, getGenesPerVar());

            for (int i = getGenesPerVar() - 1; i >= 0; i--) {
                if ((rep - (float) Math.pow(2, i)) >= 0)
                {
                    chromo.get(vars).add(Boolean.TRUE);
                    rep -= (float) Math.pow(2, i);
                }
                else {
                    chromo.get(vars).add(Boolean.FALSE);
                }
            }
        }
        
        return chromo;
    }
    
    
/*============================ Getters and Setters ===========================*/

    
    /**
     * @return the number of variables represented in the chromosome
     */
    public int getNumVars() {
        return getFitFunction().getNumVars();
    }
    
    /**
     * @return the number of genes, in bits, per variable
     */
    public int getGenesPerVar() {
        return getFitFunction().getGenesPerVar();
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
    public GATestFunction getFitFunction() {
        return fitFunction;
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
        if (!fitFunction.isMaxProblem()) {
            res += "\n  Fitness Transferral: " + fitTransValue;
        }
        
        return res;
    }
    
}
