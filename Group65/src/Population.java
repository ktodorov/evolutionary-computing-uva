import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Population {
    private double highestFitness;
    private DoubleIndividual[] people;
    private ParentSelectionType parentSelectionType;
    private RankingType rankingType;
    private double[] mean;
    private double[] standardDeviation;
    private int populationSize;

    public Population(
        ParentSelectionType parentSelectionType,
        int populationSize,
        RankingType rankingType) {

        this.parentSelectionType = parentSelectionType;
        this.people = new DoubleIndividual[populationSize];
        this.populationSize = populationSize;
        this.rankingType = rankingType;
        this.initializeMeanAndVariance();

        // Initialize each individual
        for (int i = 0; i < this.people.length; i++) {
            this.people[i] = new DoubleIndividual();
        }
    }

    public void clearPopulation(){
        this.people = new DoubleIndividual[0];
    }

    public void initializeMeanAndVariance(){
        this.mean = new double[10];
        this.standardDeviation = new double[10];
        // Initialize the means and variances
        for (int i = 0; i < 10; i++) {
            this.mean[i] = 0;
            this.standardDeviation[i] = 0;
        }
    }

    public Population(
        ParentSelectionType parentSelectionType,
        DoubleIndividual[] individuals,
        RankingType rankingType) {

        this.parentSelectionType = parentSelectionType;
        this.people = new DoubleIndividual[0];
        this.populationSize = individuals.length;
        this.rankingType = rankingType;
        this.addIndividuals(individuals);
    }

    public DoubleIndividual[] getPeople() {
        return this.people;
    }

    public DoubleIndividual[] createNewChildren(int count) {
        DoubleIndividual[] parents = this.selectParents(count);
        return recombine(parents);
    }

    public DoubleIndividual[] mutateIndividualsByDouble(int count) {
        DoubleIndividual[] individualsForMutation = this.selectRouletteWheel(count);
        //DoubleIndividual[] individualsForMutation = this.selectRandomly(count);

        //Initialize some variables
        this.initializeMeanAndVariance();
        Random rand = new Random();
        double constantGaussian = rand.nextGaussian();
        double t1 = 1/Math.sqrt(2*Constants.DIMENSIONS);
        double t2 = 1/Math.sqrt(2*Math.sqrt(Constants.DIMENSIONS));
        double[] changingGauss = new double[10];
        for (int a = 0; a < 10; a++) {
            changingGauss[a] = rand.nextGaussian();
        }

        //Find Standard Deviation
        for (int j = 0; j < Constants.DIMENSIONS; j++) {
            for (int i = 0; i < this.people.length; i++) {
                this.mean[j] += this.people[i].getGenotypeDouble()[j];
            }
            this.mean[j] /= this.people.length;

            for (int p = 0; p < this.people.length; p++) {
                this.standardDeviation[j] += Math.pow(this.people[p].getGenotypeDouble()[j] - this.mean[j], 2);
            }
            this.standardDeviation[j] /= this.people.length-1;
            if(this.standardDeviation[j] < 0){
                System.out.print("SD is wrecked.");
            }
            this.standardDeviation[j] = Math.sqrt(this.standardDeviation[j]);
            this.standardDeviation[j] = this.standardDeviation[j] * Math.exp(t1 * constantGaussian + t2 * changingGauss[j]);
        }

        //Mutate
        for (int k = 0; k < individualsForMutation.length; k++) {
            double[] newGenotype = new double[10];
            for (int j = 0; j < Constants.DIMENSIONS; j++) {
                newGenotype[j] = individualsForMutation[k].getGenotypeDouble()[j] + this.standardDeviation[j] * changingGauss[j];

                if (newGenotype[j] < -5) {
                    newGenotype[j] = -5;
                }
                
                if (newGenotype[j] > 5) {
                    newGenotype[j] = 5;
                }
            }
            if(individualsForMutation[k].containsNaN()){
                System.out.println("before");
            }
            individualsForMutation[k].setGenotypeDouble(newGenotype);
            if(individualsForMutation[k].containsNaN()){
                System.out.println("after");
            }
        }
        return individualsForMutation;
    }

    // Note: No copies are added to the new population.
    // Beware of Individual.setEncoding for changes in #starter
    public void addIndividuals(DoubleIndividual[] individualsToAdd){
        if(this.people.length >= this.populationSize) {
            System.out.println("Population full. Abort");
        }
        else {
            int newSize = this.people.length + individualsToAdd.length;
            DoubleIndividual[] buffer = new DoubleIndividual[newSize];

            System.arraycopy(this.people, 0, buffer, 0, this.people.length);
            System.arraycopy(individualsToAdd, 0, buffer, this.people.length, newSize-this.people.length);
            this.people = buffer;
        }
    }

    public void print() {
        for (DoubleIndividual individual : this.people) {
            System.out.print(" ");
            individual.print();
        }
    }

    private void createProbabilitiesBasedOnLinearRanking(){
        sortPeopleByFitnessAscending();
        double c = Constants.K_FOR_LIN_RANKING;
        double meanFitness = 0;
        for (int i = 0; i < this.people.length; i++) {
            meanFitness += this.people[i].getFitness();
        }
        double sum = meanFitness;
        meanFitness /= this.people.length;
        for (int j = 0; j < this.people.length; j++) {
            this.people[j].setProbabilities(((2-c)/meanFitness + 2*j*(c-1)/(meanFitness*(meanFitness-1)))/sum);
        }
    }

    private void createProbabilitiesBasedOnExponentialRanking(){
        sortPeopleByFitnessAscending();
        double c = Constants.K_FOR_EXP_RANKING;
        for (int i = 0; i < this.people.length; i++) {
            this.people[i].setProbabilities(Math.pow(c, this.people.length-1-i)*(c-1) / (Math.pow(c, this.people.length)-1));
        }
    }

    /*
    SELECTION ALGORITHMS
     */
    private DoubleIndividual[] selectParents(int count) {
        switch (this.parentSelectionType){
            case RANDOM:
                return selectRandomly(count);
            case ROULETTE_WHEEL:
                return selectRouletteWheel(count);
            case TOURNAMENT:
                return selectTournament(count);
            case FITTEST:
                return selectTopIndividuals(count);
        }

        return null;
    }

    public DoubleIndividual[] selectRandomly(int count) {
        DoubleIndividual[] randomIndividuals = new DoubleIndividual[count];
        for (int k = 0; k < randomIndividuals.length; k++) {
            randomIndividuals[k] = this.people[(int)(Math.random()*this.people.length)];
        }

        return randomIndividuals;
    }

    public DoubleIndividual[] selectTopIndividuals(int count) {
        DoubleIndividual[] fittest = new DoubleIndividual[count];

        this.sortPeopleByFitnessAscending();
        Collections.reverse(Arrays.asList(this.people));
        // get the N fittest people
        for (int i = 0; i < count; i++) {
            fittest[i] = this.people[i];
        }

        return fittest;
    }

    private DoubleIndividual[] selectTournament(int count){
        //System.out.println("SELECT PARENTS BY TOURNAMENT");

        DoubleIndividual[] parents = new DoubleIndividual[count];
        DoubleIndividual[] peopleCopy = ArrayHelper.copyArray(this.people);
        for (int k = 0; k < count; k++) {
            int indexOfHighest = -1;
            double fitnessOfHighest = -100;
            //Dynamic tournament size
            //System.out.println("Tournament");
            for(int i = 0; i < Constants.TOURNAMENT_SIZE; i++){
                int currentIndex = (int) (Math.random()*peopleCopy.length);
                double currentFitness = peopleCopy[currentIndex].getFitness();
                //System.out.println(currentFitness);
                if(currentFitness > fitnessOfHighest || indexOfHighest == -1){
                    fitnessOfHighest = currentFitness;
                    indexOfHighest = currentIndex;
                }
            }
            //System.out.print("highest fitness ");
            //System.out.println(fitnessOfHighest);
            parents[k] = peopleCopy[indexOfHighest];
            //parents[k].printGenotype();
            //parents[k].print();
        }

        return parents;
    }

    private DoubleIndividual[] selectRouletteWheel(int count) {
        // initialize new array for parents
        DoubleIndividual[] parents = new DoubleIndividual[count];

        // create probabilities for rank based roulette and sort the people in descending order
        if (this.rankingType == RankingType.LINEAR) {
            this.createProbabilitiesBasedOnLinearRanking();
        }
        else {
            this.createProbabilitiesBasedOnExponentialRanking();
        }
        
        DoubleIndividual[] peopleCopy = ArrayHelper.copyArray(this.people);
        double overallFitness = this.calculateOverallFitness();
        int currentMember = 0;
        double r;
        int k;
        while (currentMember < count){
            r = Math.random();
            k = 0;
            double cumulativeProb = 0;
            while(cumulativeProb < r && k < peopleCopy.length){
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
        //System.out.print("Roulette-While took: ");
        //System.out.print((System.nanoTime()-time1));
        //System.out.println("nanos.");

        return parents;
    }

    private DoubleIndividual[] recombine(DoubleIndividual[] parents) {
        //TODO which parents mate with each other? neighborhood relation on sorted or randomly shuffled array?
        ArrayHelper.shuffleArray(parents);
        DoubleIndividual[] children = new DoubleIndividual[parents.length];
        for (int k = 0; k < parents.length - 1; k+=2) {
            //DoubleIndividual[] newChildren = DoubleIndividual.recombineIndividualsByWholeArithmetic((DoubleIndividual) parents[k], (DoubleIndividual) parents[k + 1]);
            DoubleIndividual[] newChildren = DoubleIndividual.recombineIndividualBySwappingTails(parents[k], parents[k + 1]);
            children[k] = newChildren[0];
            children[k+1] = newChildren[1];
        }

        return children;
    }

    public void recalculateFitness() {
        double highest = -1000;
        double current;
        for (DoubleIndividual individual : this.people) {
            current = individual.calculateFitness();
            if(current > highest){
                highest = current;
            }
        }
        this.highestFitness = highest;
    }

    // Calculate the sum over all individuals' fitnesses
    private double calculateOverallFitness(){
        double result = 0;
        for (DoubleIndividual individual : this.people) {
            result += individual.getFitness();
        }

        return result;
    }

    private void sortPeopleByFitnessAscending(){
       Arrays.sort(this.people, Comparator.comparingDouble(DoubleIndividual::getFitness));
    }
    //private void sortPeopleByFitnessDescending(){
    //    Arrays.sort(this.people, Comparator.comparingDouble(DoubleIndividual::getFitness).reversed());
    //}

    public double getHighestFitness(){
        return this.highestFitness;
    }

    public double getAverage(){
        return calculateOverallFitness() / this.people.length;
    }

    public double getStandardDeviation(){
        double average = getAverage();

        double numerator = 0.0;
        for (DoubleIndividual individual : this.people) {
            numerator += Math.pow((individual.getFitness() - average), 2);
        }

        double result = Math.sqrt(numerator / (this.people.length - 1));
        return result;
    }
}