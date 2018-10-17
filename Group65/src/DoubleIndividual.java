public class DoubleIndividual {
    public double[] phenotype;
    public double[] genotype;
    private double probabilities;
    private double fitness;

    public DoubleIndividual() {
        this.phenotype = new double[10];
        this.genotype = new double[10];
        this.probabilities = 0;
        this.initRandomGenotype();
    }
    private void initRandomGenotype(){
        for (int i = 0; i < 10; i++) {
            this.genotype[i] = (Math.random() * Constants.DIMENSIONS) - 5; //Randomly distributed between [-5, 5]
            this.phenotype[i] = this.genotype[i];
        }
    }

    public static DoubleIndividual[] recombineIndividualsByWholeArithmetic(DoubleIndividual firstParent, DoubleIndividual secondParent) {
        //average between two parents by random probabilities x and 1-x. Use same probability for all 10 dimensions
        double p_x = Math.random();
        double p_y = 1 - p_x;

        DoubleIndividual firstIndividual = new DoubleIndividual();
        DoubleIndividual secondIndividual = new DoubleIndividual();

        for (int i = 0; i < 10; i++) {
            firstIndividual.phenotype[i] = p_x * firstParent.phenotype[i] + p_y * secondParent.phenotype[i];
            firstIndividual.genotype[i] = firstIndividual.phenotype[i];
            
            secondIndividual.phenotype[i] = p_x * secondParent.phenotype[i] + p_y * firstParent.phenotype[i];
            secondIndividual.genotype[i] = secondIndividual.phenotype[i];
        }

        DoubleIndividual[] result = new DoubleIndividual[2];
        result[0] = firstIndividual;
        result[1] = secondIndividual;

        return result;
    }

    public static DoubleIndividual[] recombineIndividualBySwappingTails(DoubleIndividual firstParent, DoubleIndividual secondParent) {
        DoubleIndividual firstIndividual = new DoubleIndividual();
        DoubleIndividual secondIndividual = new DoubleIndividual();

        int swappingPosition = (int)((Math.random() * 7) + 1);

        for (int i = 0; i < 10; i++) {
            if (i < swappingPosition) {
                firstIndividual.phenotype[i] = firstParent.phenotype[i];
                firstIndividual.genotype[i] = firstIndividual.phenotype[i];
                
                secondIndividual.phenotype[i] = secondParent.phenotype[i];
                secondIndividual.genotype[i] = secondIndividual.phenotype[i];
            }
            else {
                firstIndividual.phenotype[i] = secondParent.phenotype[i];
                firstIndividual.genotype[i] = firstIndividual.phenotype[i];
                
                secondIndividual.phenotype[i] = firstParent.phenotype[i];
                secondIndividual.genotype[i] = secondIndividual.phenotype[i];
            }
        }

        DoubleIndividual[] result = new DoubleIndividual[2];
        result[0] = firstIndividual;
        result[1] = secondIndividual;

        return result;
    }

    public void print(){
        System.out.print("FITNESS:");
        System.out.println(this.fitness);
    }

    public void setProbabilities(double p){
        this.probabilities = p;
    }
    public double getProbabilities(){
        return this.probabilities;
    }
    public void setGenotypeDouble(double[] a){
        this.genotype = a;
        this.phenotype = this.genotype;
    }
    public double[] getGenotypeDouble(){
        return this.genotype;
    }

    public boolean containsNaN(){
        for (int i = 0; i < this.genotype.length-1; i++) {
            if(Double.isNaN(genotype[i])){
                return true;
            }
        }
        return false;
    }

    public void printGenotype(){
        System.out.print("[");
        for (int i = 0; i < this.genotype.length-1; i++) {
            System.out.print(this.genotype[i]);
            System.out.print(", ");
        }
        System.out.print(this.genotype[9]);
        System.out.println("]");
    }

    public double getFitness(){
        return this.fitness;
    }
    public static int counter = 0;

    protected double calculateFitness() {
        Evolution.FITNESS_EVALUATIONS++; //Easier to track evaluations during testing
        this.fitness = (double) Evolution.eval.evaluate(this.genotype);
        // if (Double.isNaN(this.fitness)) {
        //     System.out.println("WRONG INDIVIDUAL:");
        //     this.printGenotype();
        // }

        this.fitness = Double.isNaN(this.fitness) ? -42 : this.fitness;
        if(this.fitness == -42){
            //this.initRandomGenotype();
            //System.out.println("Some Genotype is null.");
        }
        return this.fitness;
    }
}