import Helpers.MathHelper;
import java.util.Collections;
import Constants.Constants;

public class Evolution {
    public static void startEvolutionaryAlgorithm(){
        //PSEUDOCODE EVOLUTIONARY ALGORITHM
        //INITIALIZE
        //EVALUATE
        //REPEAT UNTIL TERMINATION CONDITION
        // SELECT PARENTS
        // RECOMBINE
        // MUTATE
        // EVALUATE
        // SELECT FOR NEXT GEN

        Population germanPopulation = new Population();
        for (int i = 0; i < Constants.CYCLES_SIZE; i++) {
            printPopulationStats(germanPopulation);

            // Select parents
            Individual[] bufferParents = germanPopulation.selectParents();
            // printIndividualsArray(bufferParents, "Parents");            

            // Create children
            Individual[] bufferChildren = createChildrenFromParents(bufferParents);
            // printIndividualsArray(bufferChildren, "Children");            

            // Replace current worst individuals with the new children
            germanPopulation.replaceWorstIndividuals(bufferChildren);
        }
    }

    public static void printPopulationStats(Population population) {
        System.out.println("Overall population fitness: " + population.getOverallFitness());
        System.out.println("Highest individual fitness: " + population.highestIndividualFitness());
    }

    private static void printIndividualsArray(Individual[] individuals, String header){
        System.out.println(header + ":");
        
        for (Individual p : individuals) {
            System.out.print(" ");
            p.print();
        }
    }

    private static Individual[] createChildrenFromParents(Individual[] parents) {
        // Recombine first n - 1 children out of n parents
        Individual[] children = new Individual[Constants.PARENTS_SWAP_SIZE];
        for (int k = 0; k < Constants.PARENTS_SWAP_SIZE - 1; k++) {
            children[k] = new Individual(parents[k], parents[k+1]);
        }

        // Mutate last child by mutating last parent
        // TODO: combine binary/decimal conversion in mutate method?
        Individual lastParent = parents[Constants.PARENTS_SWAP_SIZE - 1];
        children[Constants.PARENTS_SWAP_SIZE - 1] = new Individual(lastParent, Constants.DEFAULT_MUTATION_TYPE);

        return children;
    }
}