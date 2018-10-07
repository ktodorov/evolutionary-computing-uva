public class DoubleIndividual extends BaseIndividual {
    private double[] phenotype;
    private double[] genotype;
    
    public DoubleIndividual() {
        this.phenotype = new double[10];
        this.genotype = new double[10];
        for (int i = 0; i < 10; i++) {
            this.phenotype[i] = Math.random() * Constants.MAX_PHENOTYPE_NUMBER_SIZE;
            this.genotype[i] = this.phenotype[i];
        }
        this.fitness = calculateFitness();
    }

    public DoubleIndividual(DoubleIndividual firstParent, DoubleIndividual secondParent) {
        //average between two parents by random probabilities x and 1-x. Use same probability for all 10 dimensions
        double p_x = Math.random();
        double p_y = 1 - p_x;
        this.phenotype = new double[10];
        this.genotype = new double[10];

        for (int i = 0; i < 10; i++) {
            this.phenotype[i] = p_x * firstParent.phenotype[i] + p_y * secondParent.phenotype[i];
            this.genotype[i] = this.phenotype[i];
        }
        this.fitness = calculateFitness();
    }

    public void mutate(MutationType mutationType) {
        for (int i = 0; i < 10; i++) {
            this.genotype[i] = MutationsHelper.mutateByType(this.genotype[i], mutationType);
            this.phenotype[i] = this.genotype[i];
        }
        this.fitness = calculateFitness();
    }

    //Messy for 10 dimensions

    public void print(){
        String printer = "[";
        for (int i = 0; i < 10; i++) {
            printer += this.genotype[i]+"; ";
        }
        System.out.println(printer +"], Fitness: " + this.fitness);
    }


    protected double calculateFitness() {
        //don't run this too often!
        return (double) Evolution.eval.evaluate(this.genotype);

        //Our testing
        /*
        double result = 0;
        for (double value : this.phenotype) {
            result += value*value;
        }
        return result;
        */
    }
}