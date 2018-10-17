public class Constants{

    public final static int DIMENSIONS = 10;

    public final static int POPULATION_SIZE = 34;

    //ALWAYS CHOOSE AN EVEN NUMBER FOR RECOMBINATION
    public final static int RECOMBINATION_SIZE = 16;

    public final static int MUTATION_SIZE = 12;

    public final static int FITTEST_SIZE = POPULATION_SIZE - RECOMBINATION_SIZE - MUTATION_SIZE;

    public final static ParentSelectionType CURRENT_PARENT_SELECTION_TYPE = ParentSelectionType.ROULETTE_WHEEL;
    
    public final static RankingType DEFAULT_RANKING_TYPE = RankingType.LINEAR;

    public final static int TOURNAMENT_SIZE = 3;

    public final static double K_FOR_EXP_RANKING = 0.5;

    public final static double K_FOR_LIN_RANKING = 2;
}