import Constants.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Population {
    private Individual[] people;
    private double overallFitness;

    Population(){
        this.people = new Individual[Constants.POPULATION_SIZE];
        
        // Initialize each individual
        for (int i = 0; i < this.people.length; i++) {
            this.people[i] = new Individual();
        }
    }

    //Note: Not sure if actual copies are added to the new population or just references
    //Beware of Individual.setEncoding for changes in #starter
    Population(Individual[] starter){
        this.people = new Individual[starter.length];
        System.arraycopy(starter, 0, this.people, 0, starter.length);
    }

    //Note: No copies are added to the new population.
    //Beware of Individual.setEncoding for changes in #starter
    void addIndividualsToPop(Individual[] individualsToAdd){
        if(this.people.length >= Constants.POPULATION_SIZE){
            System.out.println("Population full. Abort");
        }
        else{
            int newSize = this.people.length+individualsToAdd.length;
            Individual[] buffer = new Individual[newSize];

            System.arraycopy(this.people, 0, buffer, 0, this.people.length);
            System.arraycopy(individualsToAdd, 0, buffer, this.people.length, newSize-this.people.length);
            this.people = buffer;
        }
    }

    private void removeIndividualAtIndex(int index){
        if(this.people.length <= 0){
            System.out.println("Population empty. Abort");
        }
        else{
            int newSize = this.people.length-1;
            Individual[] buffer = new Individual[newSize];
            System.arraycopy(this.people, 0, buffer, 0, index);
            System.arraycopy(this.people, index+1, buffer, index, this.people.length-index-1);
            this.people = buffer;
        }
    }
    void print(){
        int k = 0;

        for (Individual i: this.people) {
            System.out.println("Fitness of individual "+(k++)+": "+i.getFitness());
        }
        System.out.println("--");
    }

    private Individual[] getPeople(){
        return this.people;
    }

    double getHighestIndividualFitness(){
        double bestFitness = -1.0;
        for (Individual individual : this.people) {
            if(individual.getFitness() > bestFitness){
                bestFitness = individual.getFitness();
            }
        }
        return bestFitness;
    }

    double getOverallFitness(){
        this.calculateOverallFitness();
        return this.overallFitness;
    }

    private void replaceIndividual(int index, Individual individual){
        this.people[index].setEncoding(individual.getEncoding());
    }

    //TODO create abstract SelectionType class for different classes? "random", "roulette wheel", "tournament"...?
    //This method returns the parents using the roulette wheel algorithm,
    //while removing them from the Population it is called on.
    Individual[] selectRouletteWheel(){

        //initialize new array for parents
        Individual[] parents = new Individual[Constants.POPULATION_SIZE];
        for (int b = 0; b < parents.length; b++) {
            //dummy values
            parents[b] = new Individual(0);
        }

        //is this necessary? Do we need a copy?
        Population copy = this;//new Population(this.getPeople());
        copy.calculateProbabilities();

        //sort the people
        sortIndividuals(copy.getPeople());

        //roulette wheel algorithm for selection based on fitness probabilites
        copy.calculateProbabilities();
        int currentMember = 0;
        while (currentMember < Constants.PARENTS_SIZE){
            double r = Math.random();
            int k = 0;
            double cumulativeProb = 0;
            while(cumulativeProb < r){
                cumulativeProb += copy.getPeople()[k].getProbability();
                k++;
            }
            k--;

            //parent at position k was found and is added to final array
            parents[currentMember] = copy.getPeople()[k];

            //prevent choosing Individuals twice
            copy.removeIndividualAtIndex(k);
            copy.calculateProbabilities();
            currentMember++;
        }
        return parents;
    }
    //This method returns randomly selected parents from the remaining population,
    Individual[] selectRandom(int amount){
        Individual[] selection = new Individual[amount];
        for (int k = 0; k < selection.length; k++) {
            selection[k] = this.getPeople()[(int)(Math.random()*this.getPeople().length)];
        }
        return selection;
    }
    //For direct selection into new generation, we pick x fittest people from previous!
    Individual[] selectFittest(){
        Individual[] fittest = new Individual[Constants.FITTEST_SIZE];
        Population buffer = new Population(this.getPeople());

        //choose the x fittest people
        for (int i = 0; i < Constants.FITTEST_SIZE; i++) {
            double maxFitness = -1;
            int indexOfFittest = -1;
            Individual candidate = new Individual(0);

            //find fittest of generation
            for (int j = 0; j < buffer.getPeople().length; j++) {
                if(buffer.getPeople()[j].getFitness() > maxFitness){
                    candidate = buffer.getPeople()[j];
                    maxFitness = candidate.getFitness();
                    indexOfFittest = j;
                }
            }
            buffer.removeIndividualAtIndex(indexOfFittest);
            fittest[i] = candidate;
        }
        return fittest;
    }

    //Calculate the sum over all individuals' fitnesses
    private double calculateOverallFitness(){
        double result = 0;
        for (Individual individual : this.people) {
            result += individual.getFitness();
        }
        this.overallFitness = result;
        return this.overallFitness;
    }

    //Calculate and set the probability for each individual based on its fitness
    private void calculateProbabilities(){
        for (int i = 0; i < this.getPeople().length; i++){
            this.getPeople()[i].setProbability(this.getPeople()[i].calculateFitness()/this.calculateOverallFitness());
        }
    }

    private void sortIndividuals(Individual[] individuals){
        Arrays.sort(individuals, Comparator.comparingDouble(Individual::getFitness).reversed());

    }

    private static int getIndexOfWorstIndividual(Individual[] individuals, ArrayList<Integer> indexesToSkip) {
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
    public void replaceWorstIndividuals(Individual[] children) {
        ArrayList<Integer> indexesToSkip = new ArrayList<Integer>();

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
}
