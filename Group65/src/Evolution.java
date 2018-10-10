import org.vu.contest.ContestEvaluation;

public class Evolution {
    public static ContestEvaluation eval;
    static void startEvolutionaryAlgorithm(ContestEvaluation evaluation, int eval_limit) {
        eval= evaluation;
        
        Population tribe = new Population(
            Constants.CURRENT_MUTATION_TYPE,
            Constants.CURRENT_PARENT_SELECTION_TYPE,
            Constants.CURRENT_PHENOTYPE_REPRESENTATION,
            Constants.CURRENT_GENOTYPE_REPRESENTATION);
        int fittestSize = Constants.FITTEST_SIZE;
        int recombinationSize = Constants.RECOMBINATION_SIZE;
        int mutationSize = Constants.MUTATION_SIZE;
        int last_cycles_without_mutation = (eval_limit/Constants.POPULATION_SIZE)/10;
        for (int i = 0; i < eval_limit/Constants.POPULATION_SIZE; i++) {
            //System.out.print("Generation ");
            //System.out.println(i);

            // If we reach the last LAST_CYCLES_WITHOUT_MUTATION cycles, 
            // we must stop mutating in order to preserve the currently found good population
            if (eval_limit/Constants.POPULATION_SIZE - i < last_cycles_without_mutation) {
                fittestSize = Constants.FITTEST_SIZE + Constants.MUTATION_SIZE;
                mutationSize = 0;
            }
            //System.out.print("\nGENERATION ");
            //System.out.println(i);
            //System.out.println("EVALUATIONS");
            //System.out.println(Constants.FITNESS_EVALUATIONS);
            tribe.recalculateFitness();
            //tribe.print();
            Population nextGeneration = new Population(
                Constants.CURRENT_MUTATION_TYPE,
                Constants.CURRENT_PARENT_SELECTION_TYPE);

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