import org.vu.contest.ContestEvaluation;

import java.sql.SQLOutput;

/**
 * OPTIMIZATION FINDINGS:
 * Evaluating fitness takes ages, but can't be optimized ->  Not our code! [50-60%]
 * Recombination takes most of the remaining time [20-30%]
 * Mutation is relatively easy [5-10%]
 * The rest is negligible
 */

public class Evolution {
    public static int FITNESS_EVALUATIONS = 0;

    public static ContestEvaluation eval;
    static void startEvolutionaryAlgorithm(ContestEvaluation evaluation, int eval_limit) {
        eval= evaluation;

        int populationSize = Constants.POPULATION_SIZE;
        int initialFittestSize = Constants.FITTEST_SIZE;
        int initialRecombinationSize = Constants.RECOMBINATION_SIZE;
        int initialMutationSize = Constants.MUTATION_SIZE;
        RankingType rankingType = Constants.DEFAULT_RANKING_TYPE;
        ParentSelectionType parentSelectionType = Constants.CURRENT_PARENT_SELECTION_TYPE;

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
            initialRecombinationSize = Integer.parseInt(recombinationSizeString);
        }

        String mutationSizeString = System.getProperty("mutationSize");
        if (mutationSizeString != null && !mutationSizeString.isEmpty()) {
            initialMutationSize = Integer.parseInt(mutationSizeString);
        }

        String parentSelectionTypeString = System.getProperty("parentSelectionType");
        if (parentSelectionTypeString != null && !parentSelectionTypeString.isEmpty()) {
            parentSelectionType = ParentSelectionType.valueOf(parentSelectionTypeString);
        }

        String rankingTypeString = System.getProperty("rankingType");
        if (rankingTypeString != null && !rankingTypeString.isEmpty()) {
            rankingType = RankingType.valueOf(rankingTypeString);
        }

        Population tribe = new Population(parentSelectionType, populationSize, rankingType);
        
        int cycles = eval_limit / populationSize;
        int last_cycles_without_mutation = cycles / 20;
        int fittestSize = initialFittestSize;
        int mutationSize = initialMutationSize;
        int recombinationSize = initialRecombinationSize;

        double previousCycleFitness = -1000;
        int maxFitnessCycle = 0;
        double maxFitness = 0.0;

        for (int i = 0; i < cycles; i++) {
            // If we reach the last last_cycles_without_mutation cycles, 
            // we must stop mutating in order to preserve the currently found good population
            if (cycles - i < last_cycles_without_mutation) {
                if (fittestSize > 0) {
                    fittestSize = initialFittestSize + mutationSize;
                    mutationSize = 0;
                }
                else {
                    recombinationSize = initialRecombinationSize + mutationSize;
                    mutationSize = 0;
                }
            }

            tribe.recalculateFitness();
            double currentHighestFitness = tribe.getHighestFitness();
            if(currentHighestFitness > maxFitness) {
                maxFitness = currentHighestFitness;
                maxFitnessCycle = i;
            }

            System.out.println(maxFitness);

            Population nextGeneration = new Population(parentSelectionType, populationSize, rankingType);
            nextGeneration.clearPopulation();

            // RECOMBINATION
            if (recombinationSize > 0) {
                DoubleIndividual[] newChildren = tribe.createNewChildren(recombinationSize);
                nextGeneration.addIndividuals(newChildren);
            }

            // MUTATION
            if (mutationSize > 0) {
                DoubleIndividual[] mutatedChildren = tribe.mutateIndividualsByDouble(mutationSize);
                nextGeneration.addIndividuals(mutatedChildren);
            }

            if (fittestSize > 0) {
                DoubleIndividual[] fittestIndividuals = tribe.selectTopIndividuals(fittestSize);
                nextGeneration.addIndividuals(fittestIndividuals);
            }

            tribe = nextGeneration;
        }

        // System.out.println("Max fitness found: " + maxFitness);
        System.out.println(maxFitnessCycle);
        // System.out.println("Average: " + tribe.getAverage());
        // System.out.println("Standard deviation: " + tribe.getStandardDeviation());
        // tribe.print();
    }
}