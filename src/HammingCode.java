
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by jalajkhandelwal on 05/11/2017.
 */
public class HammingCode {
    private static int r = 3;
    private static int messageLength = 200;
    private static int chunkSize = ((int) Math.pow(2, r) - r - 1);
    private static int parityChunkSize = chunkSize + r;

    public static void main(String[] args) {
//        Boolean[] test = {true,false,true,false,true,false};
//        List t = Arrays.asList(test).subList(0,4);
//        test(t);
        Random rand = new Random();
        List<Boolean> message = new ArrayList<>();
        for (int i = 0; i < chunkSize * 2; i++) {
//            System.out.println(i);
            if ((i) % chunkSize == 0) message.clear();

            message.add(rand.nextBoolean());
//            }
            System.out.println(Arrays.toString(message.toArray()));
//            message[i] = rand.nextBoolean();
        }
//
//        for (int i = 0 ; i < message.size(); i++) {
//            System.out.println(Arrays.toString(message.toArray()));
////            message.toArray();
//            List<Boolean> encodedChunk = makeChunk(message, r);
////            System.out.println();
//            System.out.println("Encoded String is "+Arrays.toString(encodedChunk.toArray()));
//            encodedChunk.set(i ,!encodedChunk.get(i));
////            System.out.println();
//            System.out.println("Flipped String is "+ Arrays.toString(encodedChunk.toArray()));
//            List<Boolean> decodedChunk = decodeChunk(encodedChunk, r);
//            System.out.println(Arrays.toString(decodedChunk.toArray()));
////            System.out.println(Arrays.toString(decodedChunk));
//            System.out.println(decodedChunk.equals(message) + "\n");
////            System.out.println(Collections.equals(decodedChunk, message)+"\n");
//        }
    }

    public static void test(List<Boolean> array) {
        array.forEach(System.out::println);
    }


    private static List<Boolean> decodeChunk(List<Boolean> array, int r) {
        List<Boolean> retArray = new ArrayList<>();
        boolean wrongExists = false;
        int total = 0;
        for (int i = 0; i < r; i++) {
            int count = 0;
            int numToJump = 1 << i;
            int startPoint = numToJump - 1;
            while (startPoint < parityChunkSize) {
                for (int j = startPoint; j < (startPoint + numToJump); j++) {
                    if (j == parityChunkSize) break;
                    if (Integer.bitCount(j + 1) != 1 && array.get(j)) count++;
                }
                startPoint += numToJump * 2;
            }
            if ((count % 2 == 0) == array.get(numToJump - 1)) {
                wrongExists = true;
                total += numToJump;
            }
        }

        if (wrongExists) {
            if ((total - 1) < parityChunkSize) {
                array.set(total - 1, !array.get(total - 1));
//                array[total-1] = !array.get(total-1);
            }
        }

        int index = 0;
        for (int i = 0; i < parityChunkSize; i++) {
            if (Integer.bitCount(i + 1) != 1) {
                retArray.add(array.get(i));
//                retArray[index++] = array[i];
            }
        }
        return retArray;
    }


    private static List<Boolean> makeChunk(List<Boolean> array, int numParityBits) {
        List<Boolean> messageArray = new ArrayList<>();
//        System.out.println("EM");
        int count = 0;
        for (int i = 0; i < parityChunkSize; i++) {
            if (Integer.bitCount(i + 1) != 1) {
//                System.out.println("i is " + array.get(count));
                messageArray.add(array.get(count++));
//                messageArray[i] = array.get(count++);
            } else messageArray.add(null);
        }
        messageArray.toArray();
//        System.out.println("stuff");
//        System.out.println(Arrays.toString(messageArray.toArray()));
        insertParity(messageArray);
        return messageArray;
    }

    private static void insertParity(List<Boolean> array) {
//        assert (array.size()!= 0);
//        System.out.println(array.size());
        array.toArray();
        for (int i = 0; i < r; i++) {
            int count = 0;
            int numToJump = 1 << i;
            int startPoint = numToJump - 1;

            while (startPoint < (parityChunkSize)) {
                for (int j = startPoint; j < (startPoint + numToJump); j++) {
                    if (j == (parityChunkSize)) break;
                    if (array.get(j) != null && array.get(j)) count++;
                }
                startPoint += numToJump * 2;

            }
            //FIX THIS
            //TODO
//            System.out.println(array.get(0));
            array.set((1 << i) - 1, !(count % 2 == 0));
        }
    }


//    private static Boolean[] decodeChunk(Boolean[] array, int r) {
//        Boolean[] retArray = new Boolean[array.length - r];
//        boolean wrongExists = false;
//        int total = 0;
//        for (int i = 0;i < r;i++){
//            int count = 0;
//            int numToJump = 1 << i;
//            int startPoint = numToJump -1;
//            while (startPoint < array.length){
//                for (int j = startPoint; j < (startPoint+numToJump);j++){
//                    if (j == array.length) break;
//                    if (Integer.bitCount(j+1)!= 1 && array[j]) count++;
//                }
//                startPoint+= numToJump* 2;
//            }
//            if ((count % 2 == 0) == array[numToJump - 1]){
//                wrongExists = true;
//                total += numToJump;
//            }
//        }
//
//        if (wrongExists){
//            if ((total-1) < array.length){
//                array[total-1] = !array[total-1];
//            }
//        }
//
//        int index = 0;
//        for (int i = 0; i < array.length;i++){
//            if (Integer.bitCount(i+1) != 1){
//                retArray[index++] = array[i];
//            }
//        }
//        return retArray;
//    }
//
//
//    private static Boolean[]makeChunk(Boolean[] array, int numParityBits) {
//        Boolean[] messageArray = new Boolean[array.length+numParityBits];
//        int count = 0;
//        for (int i = 0; i < messageArray.length;i++){
//            if (Integer.bitCount(i+1) != 1){
//                messageArray[i] = array[count++];
//            }
//        }
//        insertParity(messageArray);
//        return messageArray;
//    }
//
//    private static void insertParity(Boolean[] array) {
//        for (int i = 0;i < r;i++){
//            int count = 0;
//            int numToJump = 1 << i;
//            int startPoint = numToJump -1;
//            while (startPoint < array.length){
//            for (int j = startPoint; j < (startPoint+numToJump);j++){
//                if (j == array.length) break;
//                if (array[j]!= null && array[j]) count++;
//            }
//            startPoint+= numToJump* 2;
//
//            }
//            array[(1<<i)-1] = !(count%2 ==0);
//        }
//    }
}

