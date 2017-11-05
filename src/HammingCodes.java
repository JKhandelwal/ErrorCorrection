import java.util.Arrays;

/**
 * Created by jalajkhandelwal on 05/11/2017.
 */
public class HammingCodes {

    public static void main(String[] args) {
        boolean[] message = {true, false, true, false};

        boolean[] t =  makeChunks(message,3);
        System.out.println(Arrays.toString(t));
    }


    private static boolean[]makeChunks(boolean[] array, int numParityBits) {
        boolean[] newArray = new boolean[array.length+numParityBits];
        int count = 0;
        for (int i = 0; i < newArray.length;i++){
            if (i %2 != 0){
                newArray[i] = array[count++];
            }
        }
        return newArray;
    }
}
