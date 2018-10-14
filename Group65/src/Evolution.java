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
        
        Population tribe = new Population(
            Constants.CURRENT_PARENT_SELECTION_TYPE,
            populationSize);
        
        int cycles = eval_limit / populationSize;
        int last_cycles_without_mutation = cycles / 20;
        int fittestSize = initialFittestSize;
        int mutationSize = initialMutationSize;
        int recombinationSize = initialRecombinationSize;

        double previousCycleFitness = -1000;
        for (int i = 0; i < cycles; i++) {
            //System.out.print("GENERATION ");
            //System.out.println(i);

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
            //For testing: Percentage change of last to current generational max fitness!
            //System.out.println(Math.round(1000*((tribe.getHighestFitness()/previousCycleFitness)*100-100))/1000.0);
            //previousCycleFitness = tribe.getHighestFitness();
            //tribe.print();

            Population nextGeneration = new Population(
                Constants.CURRENT_PARENT_SELECTION_TYPE,
                populationSize);
            nextGeneration.clearPopulation();

            // RECOMBINATION
            DoubleIndividual[] newChildren = tribe.createNewChildren(recombinationSize);
            nextGeneration.addIndividuals(newChildren);

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
    }
}