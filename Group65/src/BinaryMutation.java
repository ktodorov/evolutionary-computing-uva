public class BinaryMutation{
    public static int[] mutate(int[] arrayToMutate){
        int randomIndex = (int)(Math.random() * arrayToMutate.length);
        if (arrayToMutate[randomIndex] == 1){
            arrayToMutate[randomIndex] = 0;
        }
        else{
            arrayToMutate[randomIndex] = 1;
        }

        return arrayToMutate;
    }
}