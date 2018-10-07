import org.vu.contest.ContestEvaluation;

public class Evolution {
    public static ContestEvaluation eval;
    static void startEvolutionaryAlgorithm(ContestEvaluation evaluation) {
        eval = evaluation;
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

        //System.out.println("\nInitial Execution.Population");
        //tribe.print();
        //tribe.printStats();
        //System.out.println("_,.-'love is in the air'-.,_");

        for (int i = 0; i < Constants.CYCLES_SIZE; i++) {
            int fittestSize = Constants.FITTEST_SIZE;
            int recombinationSize = Constants.RECOMBINATION_SIZE;
            int mutationSize = Constants.MUTATION_SIZE;

            // If we reach the last LAST_CYCLES_WITHOUT_MUTATION cycles, 
            // we must stop mutating in order to preserve the currently found good population
            if (Constants.CYCLES_SIZE - i < Constants.LAST_CYCLES_WITHOUT_MUTATION) {
                fittestSize += Constants.MUTATION_SIZE;
                mutationSize = 0;
            }

            BaseIndividual[] fittestIndividuals = tribe.getTopIndividuals(fittestSize);
            Population nextGeneration = new Population(
                Constants.CURRENT_MUTATION_TYPE,
                Constants.CURRENT_PARENT_SELECTION_TYPE,
                fittestIndividuals);

            // RECOMBINATION
            if (recombinationSize > 0) {
                BaseIndividual[] newChildren = tribe.createNewChildren(recombinationSize);
                nextGeneration.addIndividuals(newChildren);
            }

            // MUTATION
            if (mutationSize > 0) {
                BaseIndividual[] mutatedChildren = tribe.mutateIndividuals(Constants.MUTATION_SIZE);
                nextGeneration.addIndividuals(mutatedChildren);
            }

            // SURVIVOR SELECTION
            // implicitly done via the Execution.Population.nextGeneration
            tribe = nextGeneration;
        }

        //System.out.println("--\nFinal Execution.Population");
        //tribe.print();
        //tribe.printStats();
    }
}