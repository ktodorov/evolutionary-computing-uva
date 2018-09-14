import java.sql.SQLOutput;

public class Evolution {
    //Helper functions and main() method
    //Define Global Static Variables here
    public final static int POPULATION_SIZE = 5;
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
    public static void printBinary(int[] x){
        for (int i = 0; i < x.length; i++){
            System.out.print(x[i]);
        }
    }

    public static void main(String[] args){
        Population germans = new Population();
        germans.calculateOverallFitness();
        System.out.println("Overall fitness: "+germans.getOverallFitness());
    }
}