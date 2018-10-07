public abstract class BaseIndividual {
    protected double fitness;

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

    public static BaseIndividual createFromParents(BaseIndividual firstParent, BaseIndividual secondParent) {
        if (firstParent instanceof BinaryIndividual) {
            return new BinaryIndividual((BinaryIndividual)firstParent, (BinaryIndividual)secondParent);
        }
        else if (firstParent instanceof DoubleIndividual) {
            return new DoubleIndividual((DoubleIndividual)firstParent, (DoubleIndividual)secondParent);
        }

        return null;
    }

    public abstract void mutate(MutationType mutationType);

    public abstract void print();

    protected abstract double calculateFitness();
}