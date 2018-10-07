package Individuals;

import Helpers.*;
import Constants.Constants;
import Enums.*;

public class BinaryIndividual extends BaseIndividual {
    private double[] phenotype;
    private int[][] genotype;

    public BinaryIndividual() {
        this.phenotype = new double[10];
        for (int i = 0; i < phenotype.length; i++) {
            this.phenotype[i] = Math.random() * Constants.MAX_PHENOTYPE_NUMBER_SIZE;
        }
        this.genotype = new int[10][Constants.BINARY_REPRESENTATION_LENGTH];
        for (int i = 0; i < phenotype.length; i++) {
            this.genotype[i] = MathHelper.makeBinary((int)this.phenotype[i]);
        }
        this.fitness = calculateFitness();
    }

    public BinaryIndividual(BinaryIndividual firstParent, BinaryIndividual secondParent) {
        int[][] firstParentBody = firstParent.getGenotype();
        int[][] secondParentBody = secondParent.getGenotype();
        this.phenotype = new double[10];
        this.genotype = new int[10][Constants.BINARY_REPRESENTATION_LENGTH];

        for (int i = 0; i < 10; i++) {
            int[] recombination = new int[Constants.BINARY_REPRESENTATION_LENGTH];

            // cutoff between 1 and (the whole length - 1) to avoid swapping individuals entirely
            int cutoff = (int)(Math.random() * (Constants.BINARY_REPRESENTATION_LENGTH - 1)) + 1;
            System.arraycopy(firstParentBody[i], 0, recombination, 0, Constants.BINARY_REPRESENTATION_LENGTH - cutoff);
            System.arraycopy(secondParentBody[i], Constants.BINARY_REPRESENTATION_LENGTH - cutoff, recombination, Constants.BINARY_REPRESENTATION_LENGTH - cutoff, cutoff);

            this.phenotype[i] = MathHelper.makeDecimal(recombination);
            this.genotype[i] = recombination;
        }
        this.fitness = calculateFitness();
    }

    public int[][] getGenotype() {
        return genotype;
    }

    public void mutate(MutationType mutationType) {
        for (int i = 0; i < 10; i++) {
            this.genotype[i] = MutationsHelper.mutateByType(this.genotype[i], mutationType);
            this.phenotype[i] = MathHelper.makeDecimal(this.genotype[i]);
        }
        this.fitness = calculateFitness();
    }

    //Messy for 10 dimensions
    public void print(){
        System.out.println(
            /*"Phenotype: "+ this.phenotype +
            ", Genotype: " + MathHelper.getBinaryString(this.genotype) +
            */", Fitness: " + this.fitness);

    }


    protected double calculateFitness() {
        double result = 0;
        for (double value : this.phenotype) {
            result += value*value;
        }
        return result;
    }
}