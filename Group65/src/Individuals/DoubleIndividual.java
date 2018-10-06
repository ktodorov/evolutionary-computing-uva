package Individuals;

import Helpers.*;
import Enums.*;
import Constants.Constants;

public class DoubleIndividual extends BaseIndividual {
    private double phenotype;
    private double genotype;
    
    public DoubleIndividual() {
        this.phenotype = Math.random() * Constants.MAX_PHENOTYPE_NUMBER_SIZE;
        this.genotype = this.phenotype;
        this.fitness = calculateFitness();
    }

    public DoubleIndividual(DoubleIndividual firstParent, DoubleIndividual secondParent) {
        //average between two parents by random probabilities x and 1-x.
        double p_x = Math.random();
        double p_y = 1 - p_x;
        phenotype = p_x * firstParent.phenotype + p_y * secondParent.phenotype;
        this.fitness = calculateFitness();
    }

    public void mutate(MutationType mutationType) {
        this.genotype = MutationsHelper.mutateByType(this.genotype, mutationType);
        this.phenotype = this.genotype;
        this.fitness = calculateFitness();
    }
    
    public void print(){
        System.out.println(
            "Phenotype: "+ this.phenotype + 
            ", Genotype: " + this.genotype +
            ", Fitness: " + this.fitness);
    }

    protected double calculateFitness() {
        return (this.phenotype*this.phenotype);
    }
}