import Constants.Constants;

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

    //Note: No copies are added to the new population.
    //Beware of Individual.setEncoding for changes in #starter
    public Population(Individual[] starter){
        this.people = new Individual[starter.length];

        // Initialize each individual
        for (int i = 0; i < starter.length; i++) {
            this.people[i] = starter[i];
        }
    }

    //Note: No copies are added to the new population.
    //Beware of Individual.setEncoding for changes in #starter
    public void addIndividualsToPop(Individual[] individualstoAdd){
        if(this.people.length >= Constants.POPULATION_SIZE){
            System.out.println("Population full. Abort");
            return;
        }
        else{
            int newSize = this.people.length+individualstoAdd.length;
            Individual[] buffer = new Individual[newSize];

            for (int i = 0; i < this.people.length; i++) {
                buffer[i] = this.people[i];
            }
            for (int i = this.people.length; i < newSize; i++) {
                buffer[i] = individualstoAdd[i-this.people.length];
            }
            this.people = buffer;
        }
    }

    public void removeIndividualAtIndex(int index){
        if(this.people.length <= 0){
            System.out.println("Population empty. Abort");
            return;
        }
        else{
            int newSize = this.people.length-1;
            Individual[] buffer = new Individual[newSize];
            for (int i = 0; i < index; i++) {
                buffer[i] = this.people[i];
            }
            for (int i = index+1; i < this.people.length; i++) {
                buffer[i-1] = this.people[i];
            }
            this.people = buffer;
        }
    }
    public void print(){
        int k = 0;

        for (Individual i: this.people) {
            System.out.println("Fitness of individual "+(k++)+": "+i.getFitness());
        }
        System.out.println("--");
    }

    public Individual[] getPeople(){
        return this.people;
    }

    public double getHighestIndividualFitness(){
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

    //TODO create abstract SelectionType class for these differen classes? "random", "roulette wheel", "tournament"...?
    public Individual[] selectParents(){

        //initialize new array for parents
        Individual[] parents = new Individual[Constants.POPULATION_SIZE];
        for (int b = 0; b < parents.length; b++) {
            //dummy values
            parents[b] = new Individual(0);
        }

        //is this necessary?
        Population copy = this;//new Population(this.getPeople());
        copy.calculateProbabilities();

        //sort the people
        //TODO sort is inefficient O(n²)
        for (int i = 0; i < copy.getPeople().length; i++) {
            for (int j = 0; j < copy.getPeople().length; j++) {
                if( copy.getPeople()[i].getFitness() < copy.getPeople()[j].getFitness()){
                    Individual buffer = copy.getPeople()[i];
                    copy.getPeople()[i] = copy.getPeople()[j];
                    copy.getPeople()[j] = buffer;
                }
            }
        }
        //roulette wheel algorithm for selection based on fitness probabilites
        copy.calculateProbabilities();
        int currentMember = 0;
        double removedProbabilities = 0;
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
            removedProbabilities += copy.getPeople()[k].getProbability();
            copy.removeIndividualAtIndex(k);
            copy.calculateProbabilities();
            currentMember++;
        }
        return parents;
    }

    public Individual[] selectToMutate(){
        Individual[] selection = new Individual[Constants.MUTATION_SIZE];
        for (int k = 0; k < selection.length; k++) {
            selection[k] = this.getPeople()[(int)(Math.random()*this.getPeople().length)];
        }
        return selection;
    }
    //For direct selection into new generation, just pick x fittest people from previous!
    public Individual[] selectFittest(int x_Fittest_People){
        Individual[] fittest = new Individual[x_Fittest_People];
        Population buffer = new Population(this.getPeople());

        //choose the x fittest people
        for (int i = 0; i < x_Fittest_People; i++) {
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

    //Calculate the sum over all individuals' fitness
    private double calculateOverallFitness(){
        double result = 0;
        for (int i = 0; i < this.people.length; i++){
            result += people[i].getFitness();
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
}
