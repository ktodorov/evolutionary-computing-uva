import java.util.Random;

public class DoubleMutation {
    public static double mutate(double DoubleToMutate){
        //new normal distribution
        double c1 = 1; //constant for standard deviation of gaussian distribution
        double t1 = c1 / (Math.sqrt(Constants.POPULATION_SIZE));
        Random gaussianFactor = new Random();
        System.out.println("This is executed");
        return gaussianFactor.nextGaussian()*t1+DoubleToMutate;

        //old gaussian
        //return (gaussianFactor.nextGaussian()*2+DoubleToMutate);
    }
}
