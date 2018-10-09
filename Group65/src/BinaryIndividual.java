public class BinaryIndividual extends BaseIndividual {
    private double[] phenotype;
    private int[][] genotype;

    public BinaryIndividual() {
        this.phenotype = new double[10];
        for (int i = 0; i < phenotype.length; i++) {
            this.phenotype[i] = Math.random() * Constants.MAX_PHENOTYPE_NUMBER_SIZE;
        }
        this.genotype = new int[10][Constants.BINARY_REPRESENTATION_LENGTH];
        for (int i = 0; i < phenotype.length; i++) {
            this.genotype[i] = MathHelper.makeBinary((int)this.phenotype[i]);
        }
    }

    public static BinaryIndividual[] createNewChildren(BinaryIndividual firstParent, BinaryIndividual secondParent) {
        return recombineIndividualBySwappingTails(firstParent, secondParent);
    }

    private static BinaryIndividual[] recombineIndividualBySwappingTails(BinaryIndividual firstParent, BinaryIndividual secondParent) {
        BinaryIndividual firstIndividual = new BinaryIndividual();
        BinaryIndividual secondIndividual = new BinaryIndividual();

        int[][] firstParentBody = firstParent.getGenotype();
        int[][] secondParentBody = secondParent.getGenotype();

        for (int i = 0; i < 10; i++) {
            int[] firstRecombination = new int[Constants.BINARY_REPRESENTATION_LENGTH];
            int[] secondRecombination = new int[Constants.BINARY_REPRESENTATION_LENGTH];

            // cutoff between 1 and (the whole length - 1) to avoid swapping individuals entirely
            int cutoff = (int)(Math.random() * (Constants.BINARY_REPRESENTATION_LENGTH - 1)) + 1;
            System.arraycopy(firstParentBody[i], 0, firstRecombination, 0, Constants.BINARY_REPRESENTATION_LENGTH - cutoff);
            System.arraycopy(secondParentBody[i], 0, secondRecombination, 0, Constants.BINARY_REPRESENTATION_LENGTH - cutoff);
            System.arraycopy(secondParentBody[i], Constants.BINARY_REPRESENTATION_LENGTH - cutoff, firstRecombination, Constants.BINARY_REPRESENTATION_LENGTH - cutoff, cutoff);
            System.arraycopy(firstParentBody[i], Constants.BINARY_REPRESENTATION_LENGTH - cutoff, secondRecombination, Constants.BINARY_REPRESENTATION_LENGTH - cutoff, cutoff);

            firstIndividual.phenotype[i] = MathHelper.makeDecimal(firstRecombination);
            firstIndividual.genotype[i] = firstRecombination;

            secondIndividual.phenotype[i] = MathHelper.makeDecimal(secondRecombination);
            secondIndividual.genotype[i] = secondRecombination;
        }

        BinaryIndividual[] result = new BinaryIndividual[2];
        result[0] = firstIndividual;
        result[1] = secondIndividual;

        return result;
    }

    public int[][] getGenotype() {
        return genotype;
    }

    public void mutate(MutationType mutationType) {
        for (int i = 0; i < 10; i++) {
            this.genotype[i] = MutationsHelper.mutateByType(this.genotype[i], mutationType);
            this.phenotype[i] = MathHelper.makeDecimal(this.genotype[i]);
        }
        this.fitness = calculateFitness(false);
    }

    //Messy for 10 dimensions
    public void print(){
        System.out.println(
            /*"Phenotype: "+ this.phenotype +
            ", Genotype: " + MathHelper.getBinaryString(this.genotype) +
            */", Fitness: " + this.fitness);

    }

    public void setProbabilities(double p){

    }

    public double getProbabilities(){
        return 0;
    }

    protected double calculateFitness(boolean skipIfAlreadyCalculated) {
        if (skipIfAlreadyCalculated && this.fitness != -1) {
            return this.fitness;
        }
        double result = 0;
        for (double value : this.phenotype) {
            result += value*value;
        }
        return result;
    }

    public double[] getGenotypeDouble(){
        return null;
    }


    public void setGenotypeDouble(double[] a){
        return;
    }
}