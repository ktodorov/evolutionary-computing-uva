public class Main{
    int[] population;
    public static int POPULATION_SIZE = 5;

    public void Main(){
        this.population = new int[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++){
            this.population[i] = (int)(Math.random()*31);
        }
    }

    public static int[] makeBinary(int x, int binaryLength){
        //this function turns the input value x into a binary String of length "binaryLength"

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

    public static int makeDecimal(int[] x){
        int p = 0;
        for (int i = 0; i < x.length; i++){
            p += x[i]*Math.pow(2, x.length-i-1);
        }
        return p;
    }

    public static void printBinary(int[] x){
        for (int i = 0; i < x.length; i++){
            System.out.print(x[i]);
        }
    }

    public static int calculateFitness(int x){
        return (int)Math.sin((double)x);
    }

    public static int calculateSumFitness(int[] population){
        int result = 0;
        for (int i = 0; i < population.length; i++){
            result += calculateFitness(population[i]);
        }
        return result;
    }

    public static double[] calculateProbabilities(int[] population){
        double[] probs = new double[POPULATION_SIZE];
        int totalFitness = calculateSumFitness(population);
        //for (int i = 0 )
        return probs;
    }

    public static void main(String[] args){
        printBinary(makeBinary(8, 5));
        System.out.println();
        System.out.println(makeDecimal(makeBinary(8,5)));
    }
}