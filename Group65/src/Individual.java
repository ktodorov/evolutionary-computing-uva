import Helpers.MathHelper;
import Helpers.MutationsHelper;
import Mutations.MutationType;
import Constants.Constants;

import java.util.List;
import java.util.ArrayList;

public class Individual {
    private int encoding;
    private int[] body;

    private double fitness;
    private double probability;

    public Individual() {
        this.encoding = (int)(Math.random()*31);
        this.body = MathHelper.makeBinary(this.encoding);
        this.fitness = calculateFitness();
    }

    public Individual(Individual parent, MutationType mutationType){
        if (mutationType == null) {
            mutationType = MutationType.NONE;
        }        

        mutate(parent.getBody(), mutationType);
    }

    public Individual(Individual firstParent, Individual secondParent){
        int[] firstParentBody = firstParent.getBody();
        int[] secondParentBody = secondParent.getBody();
        int[] recombination = new int[Constants.BINARY_REPRESENTATION_LENGTH];

        // cutoff between 1 and (the whole length - 1) to avoid swapping individuals entirely
        int cutoff = (int)(Math.random() * (Constants.BINARY_REPRESENTATION_LENGTH - 1)) + 1;

        for (int m = 0; m < Constants.BINARY_REPRESENTATION_LENGTH - cutoff; m++){
            recombination[m] = firstParentBody[m];
        }

        for (int n = Constants.BINARY_REPRESENTATION_LENGTH - cutoff; n < Constants.BINARY_REPRESENTATION_LENGTH; n++){
            recombination[n] = secondParentBody[n];
        }

        this.setEncoding(MathHelper.makeDecimal(recombination));
    }

    public void mutate(int[] bodyArray, MutationType mutationType){
        int[] mutatedBody = MutationsHelper.mutateByType(bodyArray, mutationType);
        
        this.body = mutatedBody;
        this.encoding = MathHelper.makeDecimal(this.body);
        this.fitness = calculateFitness();
    }

    public void setEncoding(int x){
        this.encoding = x;
        this.body = MathHelper.makeBinary(this.encoding);
        this.fitness = calculateFitness();
    }

    public int getEncoding(){
        return this.encoding;
    }

    public int[] getBody(){
        return this.body;
    }

    public Double getFitness(){
        return this.fitness;
    }

    public double calculateFitness(){
        return Math.abs(Math.sin((double)this.encoding));
    }

    public void setProbability(double x){
        this.probability = x;
    }

    public double getProbability(){
        return this.probability;
    }

    public void print(){
        System.out.println(
            "Encoding: "+ this.encoding + 
            ", Decoding: " + MathHelper.getBinaryString(this.body) + 
            ", Fitness: " + this.fitness);
    }
}
