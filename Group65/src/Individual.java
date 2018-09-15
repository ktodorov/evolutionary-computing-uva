public class Individual {
    private int encoding;
    private double fitness;
    private double probability;

    public Individual(){
        this.encoding = (int)(Math.random()*31);
        this.fitness = calculateFitness();
        this.probability = 0;
    }
    public void printMe(){
        System.out.println("Encoding: "+this.encoding+", Decoding: "+Evolution.printBinary(Evolution.makeBinary(this.encoding, 5))+", Fitness: "+ this.fitness);
    }
    public void setEncoding(int x){
        this.encoding = x;
        this.fitness = calculateFitness();
    }
    public int getEncoding(){
        return this.encoding;
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
