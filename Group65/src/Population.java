import com.sun.tools.javac.Main;

public class Population {
    private Individual[] people;
    private double overallFitness;

    public Population(){
        this.people = new Individual[Evolution.POPULATION_SIZE];
        for (int i = 0; i < this.people.length; i++) {
            this.people[i] = new Individual();
            //Testing purposes
            //System.out.println("Fitness of individual "+(i+1)+": "+this.people[i].getFitness());
        }
    }
    public Individual[] getPeople(){
        return this.people;
    }
    //Calculate the sum over all individuals' fitness
    public void calculateOverallFitness(){
        double result = 0;
        for (int i = 0; i < this.people.length; i++){
            result += people[i].getFitness();
        }
        this.overallFitness = result;
    }
    public double getOverallFitness(){
        return this.overallFitness;
    }
    //Calculate and set the probability for each individual based on its fitness
    public void calculateProbabilities(){
        double totalFitness = this.overallFitness;
        for (int i = 0; i < Evolution.POPULATION_SIZE; i++){
            this.people[i].setProbability(this.people[i].calculateFitness()/totalFitness);
        }
    }
}
