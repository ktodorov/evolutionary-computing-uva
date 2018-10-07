package Mutations;
import java.util.Random;

public class DoubleMutation {
    public static double mutate(double DoubleToMutate){
        Random gaussianFactor = new Random();
        return (gaussianFactor.nextGaussian()*2+DoubleToMutate);
    }
}
