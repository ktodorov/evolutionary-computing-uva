public class Constants{

    public static int FITNESS_EVALUATIONS = 0;

    public final static int DIMENSIONS = 10;

    public final static int POPULATION_SIZE = 45;

    //ALWAYS CHOOSE AN EVEN NUMBER FOR RECOMBINATION
    public final static int RECOMBINATION_SIZE = 36;

    public final static int MUTATION_SIZE = 6;

    public final static int FITTEST_SIZE = POPULATION_SIZE - RECOMBINATION_SIZE - MUTATION_SIZE;

    public final static ParentSelectionType CURRENT_PARENT_SELECTION_TYPE = ParentSelectionType.ROULETTE_WHEEL;

    public final static double K_FOR_RANKED_BASED_PROBABILITIES = 0.5;
}