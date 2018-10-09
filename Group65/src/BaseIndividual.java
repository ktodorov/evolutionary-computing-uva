public abstract class BaseIndividual {
    protected double fitness = -1;

    public static BaseIndividual createIndividualForRepresentation(
        PhenotypeRepresentation phenotypeRepresentation,
        GenotypeRepresentation genotypeRepresentation) {
        if (phenotypeRepresentation == PhenotypeRepresentation.DOUBLE &&
        genotypeRepresentation == GenotypeRepresentation.BINARY) {
            return new BinaryIndividual();
        }
        else if (phenotypeRepresentation == PhenotypeRepresentation.DOUBLE &&
        genotypeRepresentation == GenotypeRepresentation.DOUBLE) {
            return new DoubleIndividual();
        }

        throw new IllegalArgumentException();
    }

    public double getFitness() {
        return this.fitness;
    }

    public static BaseIndividual[] createFromParents(BaseIndividual firstParent, BaseIndividual secondParent) {
        if (firstParent instanceof BinaryIndividual) {
            return BinaryIndividual.createNewChildren((BinaryIndividual)firstParent, (BinaryIndividual)secondParent);
        }
        else if (firstParent instanceof DoubleIndividual) {
            return DoubleIndividual.createNewChildren((DoubleIndividual)firstParent, (DoubleIndividual)secondParent);
        }

        return null;
    }

    public abstract void mutate(MutationType mutationType);

    public abstract void print();

    public abstract void setProbabilities(double p);

    public abstract double getProbabilities();

    protected abstract double calculateFitness(boolean skipIfAlreadyCalculated);

    public abstract void setGenotypeDouble(double[] a);

    public abstract double[] getGenotypeDouble();
}