import org.vu.contest.*;
import java.util.Random;
import java.util.Properties;

/*
javac -cp .\contest.jar player65.java ArrayHelper.java BaseIndividual.java BinaryIndividual.java BinaryMutation.java Constants.java DoubleIndividual.java DoubleMutation.java Evolution.java GenotypeRepresentation.java Main.java MathHelper.java MutationsHelper.java MutationType.java ParentSelectionType.java PhenotypeRepresentation.java Population.java

jar cmf MainClass.txt submission.jar player65.class ArrayHelper.class BaseIndividual.class BinaryIndividual.class BinaryMutation.class Constants.class DoubleIndividual.class DoubleMutation.class Evolution.class GenotypeRepresentation.class Main.class MathHelper.class MutationsHelper.class MutationType.class ParentSelectionType.class PhenotypeRepresentation.class Population.class '.\Population$1.class' '.\MutationsHelper$1.class'

jar uf contest.jar player65.class ArrayHelper.class BaseIndividual.class BinaryIndividual.class BinaryMutation.class Constants.class DoubleIndividual.class DoubleMutation.class Evolution.class GenotypeRepresentation.class Main.class MathHelper.class MutationsHelper.class MutationType.class ParentSelectionType.class PhenotypeRepresentation.class Population.class '.\Population$1.class' '.\MutationsHelper$1.class'
*/

public class player65 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
	public player65()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }
    
	public void run() {
		// Run your algorithm here
		/*
        int evals = 0;
        // init population
        // calculate fitness
        while(evals<evaluations_limit_){
            // Select parents
            // Apply crossover / mutation operators
            double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(child);
            evals++;
            // Select survivors
        }
		*/

        //OUR STUFF
		Evolution.startEvolutionaryAlgorithm(this.evaluation_, evaluations_limit_);

	}
}
