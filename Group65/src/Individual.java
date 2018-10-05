import Helpers.MathHelper;
import Helpers.MutationsHelper;
import Mutations.MutationType;
import Constants.Constants;

class Individual {
    private static int z = 0;
    private double encoding;
    private double phenotypeDouble;
    private int[] phenotypeBinary;

    public int index;
    private double fitness;
    private double probability;

    Individual(int dummy){
        this.encoding = (dummy*0)-1;
    }
    Individual() {
        this.index = z++;
        this.encoding = Math.random()*31;
        this.phenotypeDouble = this.encoding;
        this.phenotypeBinary = MathHelper.makeBinary((int)this.encoding);
        this.fitness = calculateFitness();
        this.probability = 0;
    }

/* IS THIS NECESSARY?
    public Individual(Individual parent, MutationType mutationType){
        if (mutationType == null) {
            mutationType = MutationType.NONE;
        }        

        mutateBinary(parent.getPhenotypeBinary(), mutationType);
    }
*/
    //TODO Currently MutationType is used for determining recombination type!
    Individual(Individual firstParent, Individual secondParent, MutationType type){
        if(type == MutationType.BINARY){
            int[] firstParentBody = firstParent.getPhenotypeBinary();
            int[] secondParentBody = secondParent.getPhenotypeBinary();
            int[] recombination = new int[Constants.BINARY_REPRESENTATION_LENGTH];

            // cutoff between 1 and (the whole length - 1) to avoid swapping individuals entirely
            int cutoff = (int)(Math.random() * (Constants.BINARY_REPRESENTATION_LENGTH - 1)) + 1;
            System.arraycopy(firstParentBody, 0, recombination, 0, Constants.BINARY_REPRESENTATION_LENGTH - cutoff);
            System.arraycopy(secondParentBody, Constants.BINARY_REPRESENTATION_LENGTH - cutoff, recombination, Constants.BINARY_REPRESENTATION_LENGTH - cutoff, cutoff);
            this.setEncoding(MathHelper.makeDecimal(recombination));
        }
        else if(type == MutationType.DOUBLE){
            //average between two parents by random probabilities x and 1-x.
            double p_x = Math.random();
            double p_y = 1-p_x;
            this.setEncoding(p_x*firstParent.getEncoding()+p_y*secondParent.getEncoding());
        }
    }

    void mutateBinary(MutationType mutationType){
        this.phenotypeBinary = MutationsHelper.mutateByType(this.phenotypeBinary, mutationType);
        this.encoding = MathHelper.makeDecimal(this.phenotypeBinary);
        this.fitness = calculateFitness();
    }
    void mutateDouble(MutationType mutationType){
        this.phenotypeDouble = MutationsHelper.mutateByType(this.phenotypeDouble, mutationType);
        this.encoding = this.phenotypeDouble;
        this.fitness = calculateFitness();
    }

    void setEncoding(double x){
        this.encoding = x;
        this.phenotypeDouble = x;
        this.phenotypeBinary = MathHelper.makeBinary((int)this.encoding);
        this.fitness = calculateFitness();
    }

    double getEncoding(){
        return this.encoding;
    }

    private int[] getPhenotypeBinary(){
        return this.phenotypeBinary;
    }

    Double getFitness(){
        return this.fitness;
    }

    double calculateFitness(){
        //return Math.abs(Math.sin((double)this.encoding));
        return (this.encoding*this.encoding);
    }

    void setProbability(double x){
        this.probability = x;
    }

    double getProbability(){
        return this.probability;
    }

    void print(){
        System.out.println(
            "Encoding: "+ this.encoding + 
            ", Decoding: " + MathHelper.getBinaryString(this.phenotypeBinary) +
            ", Fitness: " + this.fitness);
    }
}
