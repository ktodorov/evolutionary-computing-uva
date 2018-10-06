package Individuals;

import Helpers.*;
import Constants.Constants;
import Enums.*;

public class BinaryIndividual extends BaseIndividual {
    private double phenotype;
    private int[] genotype;

    public BinaryIndividual() {
        this.phenotype = Math.random() * Constants.MAX_PHENOTYPE_NUMBER_SIZE;
        this.genotype = MathHelper.makeBinary((int)this.phenotype);
        this.fitness = calculateFitness();
    }

    public BinaryIndividual(BinaryIndividual firstParent, BinaryIndividual secondParent) {
        int[] firstParentBody = firstParent.getGenotype();
        int[] secondParentBody = secondParent.getGenotype();
        int[] recombination = new int[Constants.BINARY_REPRESENTATION_LENGTH];

        // cutoff between 1 and (the whole length - 1) to avoid swapping individuals entirely
        int cutoff = (int)(Math.random() * (Constants.BINARY_REPRESENTATION_LENGTH - 1)) + 1;
        System.arraycopy(firstParentBody, 0, recombination, 0, Constants.BINARY_REPRESENTATION_LENGTH - cutoff);
        System.arraycopy(secondParentBody, Constants.BINARY_REPRESENTATION_LENGTH - cutoff, recombination, Constants.BINARY_REPRESENTATION_LENGTH - cutoff, cutoff);
        
        this.phenotype = MathHelper.makeDecimal(recombination);
        this.genotype = recombination;
        this.fitness = calculateFitness();
    }

    public int[] getGenotype() {
        return genotype;
    }

    public void mutate(MutationType mutationType) {
        this.genotype = MutationsHelper.mutateByType(this.genotype, mutationType);
        this.phenotype = MathHelper.makeDecimal(this.genotype);
        this.fitness = calculateFitness();
    }
    
    public void print(){
        System.out.println(
            "Phenotype: "+ this.phenotype + 
            ", Genotype: " + MathHelper.getBinaryString(this.genotype) +
            ", Fitness: " + this.fitness);
    }

    protected double calculateFitness() {
        return (this.phenotype*this.phenotype);
    }
}