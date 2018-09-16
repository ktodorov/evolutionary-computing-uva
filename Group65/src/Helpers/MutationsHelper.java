package Helpers;

import Mutations.MutationType;
import Mutations.BinaryMutation;

public class MutationsHelper{
    public static int[] mutateByType(int[] arrayToMutate, MutationType mutation){
        if (arrayToMutate == null || mutation == null){
            return arrayToMutate;
        }

        switch (mutation){
            case BINARY:
                return BinaryMutation.mutate(arrayToMutate);

            case NONE:
            default:
                return arrayToMutate;
        }
    }
}