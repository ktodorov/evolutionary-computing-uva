public class Individual {
    private int encoding;
    private double fitness;
    private double probability;

    public Individual(){
        this.encoding = (int)(Math.random()*31);
        this.fitness = calculateFitness();
        this.probability = 0;
    }
    public double getFitness(){
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

}
