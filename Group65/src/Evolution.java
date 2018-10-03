import Constants.Constants;
import Mutations.MutationType;

public class Evolution {
    public static void startEvolutionaryAlgorithm(){
        /*
        General idea for selection of new generation:
        Choose 60% of old generation for recombination using roulette. Apply mutation to 5% of them as well.
        Choose 20% of the remaining old generation for mutation.
        Choose the 20% strongest individuals of old generation without any modification.
        = new generation!
        */
        Population tribe = new Population();
        tribe.print();
        printPopulationStats(tribe);
        System.out.println("--magic--");
        for (int i = 0; i < Constants.CYCLES_SIZE; i++) {
            Population nextGeneration = new Population(tribe.selectFittest(Constants.FITTEST_SIZE));

            // PARENT SELECTION
            Individual[] parents = tribe.selectParents();

            // RECOMBINATION
            nextGeneration.addIndividualsToPop(recombine(parents));

            // MUTATION
            Individual[] toBeMutated = tribe.selectToMutate(); // currently random select from remaining population
            nextGeneration.addIndividualsToPop(mutate(toBeMutated, MutationType.BINARY));

            // SURVIVOR SELECTION
            // implicitly done via the Population.nextGeneration
            tribe = nextGeneration;
        }
        printPopulationStats(tribe);
        System.out.println("--");
        tribe.print();
    }

    public static void printPopulationStats(Population population) {
        System.out.println("Overall population fitness: " + population.getOverallFitness());
        System.out.println("Highest individual fitness: " + population.getHighestIndividualFitness());
    }

    private static void printIndividualsArray(Individual[] individuals, String header){
        System.out.println(header + ":");
        
        for (Individual p : individuals) {
            System.out.print(" ");
            p.print();
        }
    }

    private static Individual[] mutate(Individual[] tribe, MutationType type){
        for (int k = 0; k < tribe.length; k++) {
            tribe[k].mutate(type);
        }
        return tribe;
    }

    private static Individual[] recombine(Individual[] parents) {
        //TODO which parents mate with each other? Currently neighborhood relation!
        Individual[] children = new Individual[Constants.PARENTS_SIZE];
        for (int k = 0; k < Constants.PARENTS_SIZE - 1; k++) {
            children[k] = new Individual(parents[k], parents[k+1]);
        }
        children[Constants.PARENTS_SIZE-1] = new Individual(parents[0], parents[Constants.PARENTS_SIZE-1]);
        return children;
    }
}