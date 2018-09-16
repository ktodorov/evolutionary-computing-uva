import Helpers.MathHelper;
import Constants.Constants;
import Mutations.MutationType;

import java.util.List;
import java.util.ArrayList;

public class Population {
    private Individual[] people;
    private double overallFitness;

    public Population(){
        this.people = new Individual[Constants.POPULATION_SIZE];
        
            // Initialize each individual
        for (int i = 0; i < this.people.length; i++) {
            this.people[i] = new Individual();
        }
    }

    public void print(){
        int k = 0;

        for (Individual i: this.people) {
            System.out.println("Fitness of individual "+(k++)+": "+i.getFitness());
        }
    }

    public Individual[] getPeople(){
        return this.people;
    }

    public double highestIndividualFitness(){
        double bestFitness = -1.0;
        for (int s = 0; s < this.people.length; s++) {
            if(this.people[s].getFitness() > bestFitness){
                bestFitness = this.people[s].getFitness();
            }
        }

        return bestFitness;
    }

    public double getOverallFitness(){
        this.calculateOverallFitness();
        return this.overallFitness;
    }

    public void replaceIndividual(int index, Individual individual){
        this.people[index].setEncoding(individual.getEncoding());
    }

    public void replaceWorstIndividuals(Individual[] children) {
        List<Integer> indexesToSkip = new ArrayList<Integer>();

        // SELECT children by 'highest fitness' for next generation.
        for (int t = 0; t < children.length; t++) {
            Individual[] individuals = this.getPeople();
            if (individuals == null) {
                break;
            }

            int indexOfWorst = getIndexOfWorstIndividual(individuals, indexesToSkip);
            
            // If we already replaced this parent, we don't want to replace it again during this cycle
            indexesToSkip.add(indexOfWorst);

            // TODO: Should we make a check before replacing or we absolutely have to replace?
            if(children[t].getFitness() > individuals[indexOfWorst].getFitness()){
                this.replaceIndividual(indexOfWorst, children[t]);
            }
        }
    }

    public Individual[] selectParents(){
        // Select PARENTS_SWAP_SIZE parents from the population randomly
        Individual[] parents = new Individual[Constants.PARENTS_SWAP_SIZE];
        Individual[] currentPeople = this.getPeople();

        for (int j = 0; j < Constants.PARENTS_SWAP_SIZE; j++) {
            // NOTE: In this case one individual can be chosen twice as a parent
            Individual chosenParent = currentPeople[(int)(Math.random()*Constants.POPULATION_SIZE)];
            int chosenParentEncoding = chosenParent.getEncoding();

            parents[j] = new Individual();
            parents[j].setEncoding(chosenParentEncoding);
        }

        return parents;
    }

    //Calculate the sum over all individuals' fitness
    private void calculateOverallFitness(){
        double result = 0;
        for (int i = 0; i < this.people.length; i++){
            result += people[i].getFitness();
        }

        this.overallFitness = result;
    }

    //Calculate and set the probability for each individual based on its fitness
    private void calculateProbabilities(){
        double totalFitness = this.overallFitness;
        for (int i = 0; i < Constants.POPULATION_SIZE; i++){
            this.people[i].setProbability(this.people[i].calculateFitness()/totalFitness);
        }
    }

    private static int getIndexOfWorstIndividual(Individual[] individuals, List<Integer> indexesToSkip) {
        if (individuals == null || individuals.length <= 1){
            return 0;
        }

        int indexOfWorst = 0;
        for (int i = 1; i < individuals.length; i++) {
            if (indexesToSkip != null && indexesToSkip.contains(i)){
                continue;
            }

            if(individuals[i].getFitness() < individuals[indexOfWorst].getFitness()){
                indexOfWorst = i;
            }
        }

        return indexOfWorst;
    }
}
