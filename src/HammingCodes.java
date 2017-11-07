import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by jalajkhandelwal on 05/11/2017.
 */
public class HammingCodes {
    private static int messageLength = 6;
    public static void main(String[] args) {
//        Boolean[] message = {true, false, true, false};
//        Boolean[] message = {true, false, true, false,true,false,true,false,true,false,true};
//        messageLength = message.length
        Random r = new Random();
        Boolean[] message = new Boolean[((int)Math.pow(2,messageLength) - messageLength -1)];
        for(int i=0 ; i< message.length; i++){

            message[i] = r.nextBoolean();

        }
        for (int i = 0 ; i < message.length; i++) {
            System.out.println(Arrays.toString(message));
            Boolean[] encodedChunk = makeChunk(message, messageLength);
            System.out.println("Encoded String is " + Arrays.toString(encodedChunk));
            encodedChunk[i] = !encodedChunk[i];
//            encodedChunk[4] = !encodedChunk[4];
            System.out.println("Flipped String is " + Arrays.toString(encodedChunk));
            Boolean[] decodedChunk = decodeChunk(encodedChunk, messageLength);
            System.out.println(Arrays.toString(decodedChunk));
            System.out.println(Arrays.equals(decodedChunk, message)+"\n");
        }
    }

    private static Boolean[] decodeChunk(Boolean[] array, int messageLength) {
        Boolean[] retArray = new Boolean[array.length - messageLength];
        boolean wrongExists = false;
        int total = 0;
        for (int i = 0;i < messageLength;i++){
            int count = 0;
            int numToJump = 1 << i;
            int startPoint = numToJump -1;
            while (startPoint < array.length){
                for (int j = startPoint; j < (startPoint+numToJump);j++){
//                    System.out.println("J is " + j);
                    if (j == array.length) break;
                    if (Integer.bitCount(j+1)!= 1 && array[j]) count++;
                }
                startPoint+= numToJump* 2;

            }
//            System.out.println("count is " + count + " Num to jump is " + (numToJump-1));
//            System.out.println(count %2 ==0);
//            System.out.println(array[numToJump - 1]);
//            count % 2 == 0 1-> false 0->true !1->true !0-> false ==
            if ((count % 2 == 0) == array[numToJump - 1]){
                wrongExists = true;
                total += numToJump;
//                System.out.println("EXISTS for parity bit "+ (i+1) + " total is " + (total-1));
            }

        }


        if (wrongExists){
//            System.out.println(array[total-1]);
            if ((total-1) < array.length){
                array[total-1] = !array[total-1];
            }
//            System.out.println(array[total-1]);
        }

        int index = 0;
        for (int i = 0; i < array.length;i++){
            if (Integer.bitCount(i+1) != 1){
                retArray[index++] = array[i];
            }
        }
        return retArray;
    }


    private static Boolean[]makeChunk(Boolean[] array, int numParityBits) {
        Boolean[] messageArray = new Boolean[array.length+numParityBits];
        int count = 0;
        for (int i = 0; i < messageArray.length;i++){
            if (Integer.bitCount(i+1) != 1){
                messageArray[i] = array[count++];
            }
        }
        insertParity(messageArray);
        return messageArray;
    }

    private static void insertParity(Boolean[] array) {
        for (int i = 0;i < messageLength;i++){
            int count = 0;
            int numToJump = 1 << i;
            int startPoint = numToJump -1;
            while (startPoint < array.length){
            for (int j = startPoint; j < (startPoint+numToJump);j++){
                if (j == array.length) break;
                if (array[j]!= null && array[j]) count++;
//                System.out.println("J is " + j  +" i is "+i);
            }
            startPoint+= numToJump* 2;

            }
//            System.out.println(count);

            array[(1<<i)-1] = !(count%2 ==0);
//            if (count %2 ==0){
//                array[(1<<i)-1] = false;
//            } else {
//                array[(1<<i)-1] = true;
//            }
        }
    }
}

