import java.util.Arrays;
import java.util.Comparator;

public class Population {
    private BaseIndividual[] people;
    private MutationType mutationType;
    private ParentSelectionType parentSelectionType;

    public Population(
        MutationType mutationType, 
        ParentSelectionType parentSelectionType,
        PhenotypeRepresentation phenotypeRepresentation,
        GenotypeRepresentation genotypeRepresentation) {

        this.mutationType = mutationType;
        this.parentSelectionType = parentSelectionType;
        this.people = new BaseIndividual[Constants.POPULATION_SIZE];
        
        // Initialize each individual
        for (int i = 0; i < this.people.length; i++) {
            this.people[i] = BaseIndividual.createIndividualForRepresentation(phenotypeRepresentation, genotypeRepresentation);
        }
    }

    public Population(
        MutationType mutationType, 
        ParentSelectionType parentSelectionType,
        BaseIndividual[] individuals) {

        this.mutationType = mutationType;
        this.parentSelectionType = parentSelectionType;
        this.people = new BaseIndividual[0];
        this.addIndividuals(individuals);
    }

    public BaseIndividual[] getPeople() {
        return this.people;
    }

    public BaseIndividual[] getRandomIndividuals(int count) {
        BaseIndividual[] randomIndividuals = new BaseIndividual[count];
        for (int k = 0; k < randomIndividuals.length; k++) {
            randomIndividuals[k] = this.people[(int)(Math.random()*this.people.length)];
        }

        return randomIndividuals;
    }

    public BaseIndividual[] getTopIndividuals(int count) {
        BaseIndividual[] fittest = new BaseIndividual[count];

        this.sortPeopleByFitness();

        // get the N fittest people
        for (int i = 0; i < count; i++) {
            fittest[i] = this.people[i];
        }

        return fittest;
    }

    public BaseIndividual[] createNewChildren(int count) {
        BaseIndividual[] parents = this.selectParents(count);
        return recombine(parents);
    }

    public BaseIndividual[] mutateIndividuals(int count) {
        // Currently randomly select from population
        BaseIndividual[] individualsForMutation = this.getRandomIndividuals(count);
        
        for (BaseIndividual individual : individualsForMutation) {
            individual.mutate(mutationType);
        }

        return individualsForMutation;
    }

    public BaseIndividual[] getWorstIndividuals() {
        return null;
    }

    // Note: No copies are added to the new population.
    // Beware of Individual.setEncoding for changes in #starter
    public void addIndividuals(BaseIndividual[] individualsToAdd){
        if(this.people.length >= Constants.POPULATION_SIZE) {
            System.out.println("Execution.Population full. Abort");
        }
        else {
            int newSize = this.people.length + individualsToAdd.length;
            BaseIndividual[] buffer = new BaseIndividual[newSize];

            System.arraycopy(this.people, 0, buffer, 0, this.people.length);
            System.arraycopy(individualsToAdd, 0, buffer, this.people.length, newSize-this.people.length);
            this.people = buffer;
        }
    }

    public void print() {
        this.sortPeopleByFitness();
        for (BaseIndividual individual : this.people) {
            System.out.print(" ");
            individual.print();
        }
    }

    public void printStats() {
        double overallFitness = this.calculateOverallFitness();
        System.out.println("Overall population fitness: " + overallFitness);

        this.sortPeopleByFitness();
        System.out.println("Highest individual fitness: " + this.people[0].getFitness());

        System.out.println("Average individual fitness: " + overallFitness / this.people.length);
    }

    private void createProbabilitiesBasedOnRank(){
        sortPeopleByFitnessReversed();
        double c = 0.55;
        for (int i = 0; i < this.people.length; i++) {
            this.people[i].setProbabilities(Math.pow(c, this.people.length-1-i)*(c-1) / (Math.pow(c, this.people.length)-1));
        }
    }

    private BaseIndividual[] selectParentsByRouletteWheel(int count) {
        // initialize new array for parents
        BaseIndividual[] parents = new BaseIndividual[count];
        // for (int b = 0; b < parents.length; b++) {
        //     // dummy values
        //     parents[b] = new BaseIndividual(0);
        // }

        // sort the people
        sortPeopleByFitness();
        createProbabilitiesBasedOnRank();
        BaseIndividual[] peopleCopy = ArrayHelper.copyArray(this.people);

        double overallFitness = this.calculateOverallFitness();

        int currentMember = 0;
        while (currentMember < count){
            double r = Math.random();
            int k = 0;
            double cumulativeProb = 0;
            while(cumulativeProb < r && k >= peopleCopy.length){
                //new rank based probabilities
                double personProbability = peopleCopy[k].getProbabilities();

                //old fitness based probabilities
                //double personProbability = peopleCopy[k].getFitness() / overallFitness;

                cumulativeProb += personProbability;
                k++;
            }

            if (k > 0) {
                k--;
            }

            // parent at position k was found and is added to final array
            parents[currentMember] = peopleCopy[k];

            // prevent choosing Individuals twice
            overallFitness -= peopleCopy[k].getFitness();
            peopleCopy = ArrayHelper.removeElementFromArray(peopleCopy, k);
            
            currentMember++;
        }

        return parents;
    }

    private BaseIndividual[] selectParents(int count) {
        switch (this.parentSelectionType){
            case RANDOM:
                return getRandomIndividuals(count);
            case ROULETTE_WHEEL:
                return selectParentsByRouletteWheel(count);
        }

        return null;
    }

    private BaseIndividual[] recombine(BaseIndividual[] parents) {
        // TODO which parents mate with each other? Currently neighborhood relation!

        // 1 with 2, 2 with 3, 3 with 4, ..., n with 1. (Circular!)
        BaseIndividual[] children = new BaseIndividual[parents.length];
        for (int k = 0; k < parents.length - 1; k++) {
            children[k] = BaseIndividual.createFromParents(parents[k], parents[k + 1]);
        }
        
        children[parents.length - 1] = BaseIndividual.createFromParents(parents[0], parents[parents.length - 1]);
        return children;
    }

    // Calculate the sum over all individuals' fitnesses
    private double calculateOverallFitness(){
        double result = 0;
        for (BaseIndividual individual : this.people) {
            result += individual.getFitness();
        }

        return result;
    }

    private void sortPeopleByFitness(){
        Arrays.sort(this.people, Comparator.comparingDouble(BaseIndividual::getFitness).reversed());
    }
    private void sortPeopleByFitnessReversed(){
        Arrays.sort(this.people, Comparator.comparingDouble(BaseIndividual::getFitness));
    }
}