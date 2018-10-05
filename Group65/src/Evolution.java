import Constants.Constants;
import Mutations.MutationType;
class Evolution {
    static void startEvolutionaryAlgorithm(){
        /*
        General idea for selection of new generation:
        Choose 60% of old generation for recombination using roulette. Apply mutation to 5% of them as well.
        Choose 20% of the remaining old generation for mutation.
        Choose the 20% strongest individuals of old generation without any modification.
        = new generation!
        */
        Population tribe = new Population();
        System.out.println("\nInitial Population");
        tribe.print();
        printPopulationStats(tribe);
        System.out.println("_,.-'love is in the air'-.,_");
        for (int i = 0; i < Constants.CYCLES_SIZE; i++) {
            Population nextGeneration = new Population(tribe.selectFittest());

            // PARENT SELECTION
            Individual[] parents = tribe.selectRouletteWheel();

            // RECOMBINATION
            nextGeneration.addIndividualsToPop(recombine(parents, Constants.DEFAULT_MUTATION_TYPE));

            // MUTATION
            Individual[] toBeMutated = tribe.selectRandom(Constants.MUTATION_SIZE); // currently random select from remaining population
            nextGeneration.addIndividualsToPop(mutate(toBeMutated, Constants.DEFAULT_MUTATION_TYPE));

            // SURVIVOR SELECTION
            // implicitly done via the Population.nextGeneration
            tribe = nextGeneration;
        }
        printPopulationStats(tribe);
        System.out.println("--\nFinal Population");
        tribe.print();
    }

    private static void printPopulationStats(Population population) {
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
        for (Individual individual : tribe) {
            if (type == MutationType.BINARY) {
                individual.mutateBinary(type);
            } else if (type == MutationType.DOUBLE) {
                individual.mutateDouble(type);
            }
        }
        return tribe;
    }

    private static Individual[] recombine(Individual[] parents, MutationType type) {
            //TODO which parents mate with each other? Currently neighborhood relation!

            //1 with 2, 2 with 3, 3 with 4, ..., n with 1. (Circular!)
            Individual[] children = new Individual[Constants.PARENTS_SIZE];
            for (int k = 0; k < Constants.PARENTS_SIZE - 1; k++) {
                children[k] = new Individual(parents[k], parents[k + 1], type);
            }
            children[Constants.PARENTS_SIZE - 1] = new Individual(parents[0], parents[Constants.PARENTS_SIZE - 1], type);
            return children;
        }
}