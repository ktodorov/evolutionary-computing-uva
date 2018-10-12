import java.lang.reflect.Array;
import java.util.Random;

public class ArrayHelper {
    public static <T> T[] removeElementFromArray(T[] array, int index){
        if(array.length <= 0){
            System.out.println("Execution.Population empty. Abort");
            return array;
        }

        int newSize = array.length - 1;
        @SuppressWarnings("unchecked")
        T[] buffer = (T[])Array.newInstance(array.getClass().getComponentType(), newSize);
        System.arraycopy(array, 0, buffer, 0, index);
        System.arraycopy(array, index + 1, buffer, index, array.length - index - 1);
        return buffer;
    }

    public static <T> T[] copyArray(T[] array) {
        return array.clone();
    }

    public static <T> T[] shuffleArray(T[] array){
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            T objectToSwap = array[index];
            array[index] = array[i];
            array[i] = objectToSwap;
        }
        return array;
    }
}