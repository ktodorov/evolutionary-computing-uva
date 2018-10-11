import org.vu.contest.ContestEvaluation;

public class Evolution {
    public static ContestEvaluation eval;
    static void startEvolutionaryAlgorithm(ContestEvaluation evaluation, int eval_limit) {
        eval= evaluation;

        int populationSize = Constants.POPULATION_SIZE;
        int initialFittestSize = Constants.FITTEST_SIZE;
        int recombinationSize = Constants.RECOMBINATION_SIZE;
        int initialMutationSize = Constants.MUTATION_SIZE;

        String populationSizeString = System.getProperty("populationSize");
        if (populationSizeString != null && !populationSizeString.isEmpty()) {
            populationSize = Integer.parseInt(populationSizeString);
        }

        String fittestSizeString = System.getProperty("fittestSize");
        if (fittestSizeString != null && !fittestSizeString.isEmpty()) {
            initialFittestSize = Integer.parseInt(fittestSizeString);
        }

        String recombinationSizeString = System.getProperty("recombinationSize");
        if (recombinationSizeString != null && !recombinationSizeString.isEmpty()) {
            recombinationSize = Integer.parseInt(recombinationSizeString);
        }

        String mutationSizeString = System.getProperty("mutationSize");
        if (mutationSizeString != null && !mutationSizeString.isEmpty()) {
            initialMutationSize = Integer.parseInt(mutationSizeString);
        }
        
        
        Population tribe = new Population(
            Constants.CURRENT_MUTATION_TYPE,
            Constants.CURRENT_PARENT_SELECTION_TYPE,
            Constants.CURRENT_PHENOTYPE_REPRESENTATION,
            Constants.CURRENT_GENOTYPE_REPRESENTATION,
            populationSize);

        int cycles = eval_limit / populationSize;
        int last_cycles_without_mutation = cycles / 20;
        int fittestSize = initialFittestSize;
        int mutationSize = initialMutationSize;

        for (int i = 0; i < cycles; i++) {
            // If we reach the last last_cycles_without_mutation cycles, 
            // we must stop mutating in order to preserve the currently found good population
            if (cycles - i < last_cycles_without_mutation) {
                fittestSize = initialFittestSize + mutationSize;
                mutationSize = 0;
            }

            tribe.recalculateFitness();
            Population nextGeneration = new Population(
                Constants.CURRENT_MUTATION_TYPE,
                Constants.CURRENT_PARENT_SELECTION_TYPE,
                populationSize);

            // RECOMBINATION
            if (recombinationSize > 0) {
                BaseIndividual[] newChildren = tribe.createNewChildren(recombinationSize);
                nextGeneration.addIndividuals(newChildren);
            }

            // MUTATION
            if (mutationSize > 0) {
                BaseIndividual[] mutatedChildren = tribe.mutateIndividualsByDouble(mutationSize);
                nextGeneration.addIndividuals(mutatedChildren);
            }

            BaseIndividual[] fittestIndividuals = tribe.getTopIndividuals(fittestSize);
            nextGeneration.addIndividuals(fittestIndividuals);

            tribe = nextGeneration;
        }
    }
}