public class DoubleIndividual extends BaseIndividual {
    public double[] phenotype;
    public double[] genotype;
    private double probabilities;
    
    public DoubleIndividual() {
        this.phenotype = new double[10];
        this.genotype = new double[10];
        this.probabilities = 0;
        this.fitness = -1;

        for (int i = 0; i < 10; i++) {
            this.phenotype[i] = (Math.random() * Constants.DIMENSIONS) - 5; //Randomly distributed between [-5, 5]
            this.genotype[i] = this.phenotype[i];
        }
    }

    public static DoubleIndividual[] createNewChildren(DoubleIndividual firstParent, DoubleIndividual secondParent) {
        // return recombineIndividualsByWholeArithmetic(firstParent, secondParent);
        return recombineIndividualBySwappingTails(firstParent, secondParent);
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


    public void mutate(MutationType mutationType) {
        /*
        int i = (int) Math.random() * 9;
        this.genotype[i] = MutationsHelper.mutateByType(this.genotype[i], mutationType);
        this.phenotype[i] = this.genotype[i];
        */
        System.out.println("this shouldn't be used.");
    }

    //Messy for 10 dimensions

    public void print(){
        String printer = "[";
        for (int i = 0; i < 10; i++) {
            printer += this.genotype[i]+"; ";
        }
        System.out.println(printer +"], Fitness: " + this.fitness);
    }

    public void setProbabilities(double p){
        this.probabilities = p;
    }
    public double getProbabilities(){
        return this.probabilities;
    }
    public double[] getPhenotypeDouble(){
        return this.phenotype;
    }
    public void setGenotypeDouble(double[] a){
        this.genotype = a;
        this.phenotype = this.genotype;
    }
    public double[] getGenotypeDouble(){
        return this.genotype;
    }


    public static int counter = 0;
    protected double calculateFitness(boolean skipIfAlreadyCalculated) {
        //don't run this too often!
        if (skipIfAlreadyCalculated && this.fitness != -1) {
            return this.fitness;
        }

        //System.out.println("calculating fitness: " + counter++);
        //System.out.println(Evolution.eval.evaluate(this.genotype));
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