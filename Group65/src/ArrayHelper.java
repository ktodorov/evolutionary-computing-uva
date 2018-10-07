import java.lang.reflect.Array;

public class ArrayHelper {
    public static <T> T[] removeElementFromArray(T[] array, int index){
        if(array.length <= 0){
            System.out.println("Execution.Population empty. Abort");
            return array;
        }

        int newSize = array.length - 1;
        T[] buffer = (T[])Array.newInstance(array.getClass().getComponentType(), newSize);
        System.arraycopy(array, 0, buffer, 0, index);
        System.arraycopy(array, index + 1, buffer, index, array.length - index - 1);
        return buffer;
    }

    public static <T> T[] copyArray(T[] array) {
        return array.clone();
    }
}