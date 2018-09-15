public class Evolution {
    //Helper functions and main() method
    //Define Global Static Variables here
    public final static int POPULATION_SIZE = 10;
    public Evolution(){
    }

    //turns the input value x into a binary String of length "binaryLength"
    public static int[] makeBinary(int x, int binaryLength){

        int[] output = new int[binaryLength];
        for (int i = binaryLength-1; i > 0; i--){
            if (x%2==0){
                output[i] = 0;
            }
            else{
                output[i] = 1;
            }
            x = (int)(Math.floor((double)(x/2)));
        }
        return output;
    }
    //turns a binary number in int[] format into a decimal number as an Integer
    public static int makeDecimal(int[] x){
        int p = 0;
        for (int i = 0; i < x.length; i++){
            p += x[i]*Math.pow(2, x.length-i-1);
        }
        return p;
    }

    //print a binary to the console for testing purposes
    public static String printBinary(int[] x){
        String result = "";
        for (int i = 0; i < x.length; i++){
            result += x[i];
        }
        return result;
    }
    public static int[] mutate(int[] x){
        int randomIndex = (int)(Math.random()*x.length);
        if (x[randomIndex] == 1){
            x[randomIndex] = 0;
        }
        else{
            x[randomIndex] = 1;
        }
        return x;
    }

    public static void main(String[] args){
        //PSEUDOCODE EVOLUTIONARY ALGORITHM
        //INITIALIZE
        //EVALUATE
        //REPEAT UNTIL TERMINATION CONDITION
        // SELECT PARENTS
        // RECOMBINE
        // MUTATE
        // EVALUATE
        // SELECT FOR NEXT GEN

        Population germans = new Population();
        for (int i = 0; i < 5; i++) {
            System.out.println("Overall population fitness: " + germans.getOverallFitness());
            System.out.println("Highest individual fitness: " + germans.highestIndividualFitness());
            //select 5 parents randomly
            Individual[] bufferParents = new Individual[5];
            for (int j = 0; j < 5; j++) {
                bufferParents[j] = new Individual();
                //NOTE: In this case one individual can be chosen twice as a parent
                bufferParents[j].setEncoding(germans.getPeople()[(int)(Math.random()*POPULATION_SIZE)].getEncoding());
            }

            //Testing purposes
            /*
            System.out.println("parents");
            for (Individual o : bufferParents) {
                o.printMe();
            }
            */
            //RECOMBINE first 4 children out of 5 parents
            Individual[] bufferChildren = new Individual[5];
            //cutoff between 1 and 4 to avoid swapping individuals entirely
            int cutoff = (int)(Math.random()*4)+1;
            //System.out.println("Cutoff: "+cutoff);
            for (int k = 0; k < 5-1; k++) {
                bufferChildren[k] = new Individual();
                int[] parent1 = makeBinary(bufferParents[k].getEncoding(), 5);
                int[] parent2 = makeBinary(bufferParents[k+1].getEncoding(), 5);
                int[] recombination = new int[5];
                for (int m = 0; m < 5-cutoff; m++){
                       recombination[m] = parent1[m];
                }
                for (int n = 5-cutoff; n < 5; n++){
                    recombination[n] = parent2[n];
                }
                bufferChildren[k].setEncoding(makeDecimal(recombination));
            }
            //MUTATE last child by mutating last parent
            //Todo: combine binary/decimal conversion in mutate method?
            bufferChildren[4] = new Individual();
            bufferChildren[4].setEncoding(makeDecimal(mutate(makeBinary(bufferParents[4].getEncoding(), 5))));

            //Testing purposes
            /*
            System.out.println("children");
            for (Individual p : bufferChildren) {
                p.printMe();
            }
            */

            //SELECT children by 'highest fitness' for next generation.
            for (int t = 0; t < bufferChildren.length; t++) {
                int indexOfWorst = 0;
                for (int s = 0; s < germans.getPeople().length; s++) {
                    if(germans.getPeople()[s].getFitness() < germans.getPeople()[indexOfWorst].getFitness()){
                        indexOfWorst = s;
                    }
                }
                if(bufferChildren[t].getFitness() > germans.getPeople()[indexOfWorst].getFitness()){
                    germans.getPeople()[indexOfWorst].setEncoding(bufferChildren[t].getEncoding());
                }
            }
        }
    }
}