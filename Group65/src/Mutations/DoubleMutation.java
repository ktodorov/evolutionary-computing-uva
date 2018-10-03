package Mutations;
import java.util.Random;

//NOT USED ATM
public class DoubleMutation {
    public static double mutate(double DoubleToMutate){
        Random gaussianFactor = new Random();
        return (gaussianFactor.nextGaussian()*5+DoubleToMutate);
    }
}
