import Constants.Constants;
import Enums.*;
import Individuals.BaseIndividual;

class Evolution {
    static void startEvolutionaryAlgorithm() {
        /*
        General idea for selection of new generation:
        Choose 60% of old generation for recombination using roulette. Apply mutation to 5% of them as well.
        Choose 20% of the remaining old generation for mutation.
        Choose the 20% strongest individuals of old generation without any modification.
        = new generation!
        */
        
        Population tribe = new Population(
            Constants.CURRENT_MUTATION_TYPE,
            Constants.CURRENT_PARENT_SELECTION_TYPE,
            Constants.CURRENT_PHENOTYPE_REPRESENTATION,
            Constants.CURRENT_GENOTYPE_REPRESENTATION);

        System.out.println("\nInitial Population");
        tribe.print();
        tribe.printStats();
        System.out.println("_,.-'love is in the air'-.,_");

        for (int i = 0; i < Constants.CYCLES_SIZE; i++) {
            BaseIndividual[] fittestIndividuals = tribe.getTopIndividuals(Constants.FITTEST_SIZE);
            Population nextGeneration = new Population(
                Constants.CURRENT_MUTATION_TYPE,
                Constants.CURRENT_PARENT_SELECTION_TYPE,
                fittestIndividuals);

            // RECOMBINATION
            BaseIndividual[] newChildren = tribe.createNewChildren();
            nextGeneration.addIndividuals(newChildren);

            // MUTATION
            BaseIndividual[] mutatedChildren = tribe.mutateIndividuals(Constants.MUTATION_SIZE);
            nextGeneration.addIndividuals(mutatedChildren);

            // SURVIVOR SELECTION
            // implicitly done via the Population.nextGeneration
            tribe = nextGeneration;
        }

        System.out.println("--\nFinal Population");
        tribe.print();
        tribe.printStats();
    }
}