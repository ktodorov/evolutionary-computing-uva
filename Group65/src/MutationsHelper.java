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

    public static double mutateByType(double encoding, MutationType mutation){
        if (encoding == 0 || mutation == null){
            return encoding;
        }

        switch (mutation){
            case DOUBLE:
                return DoubleMutation.mutate(encoding);
            case NONE:
            default:
                return encoding;
        }
    }
}