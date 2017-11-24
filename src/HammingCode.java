import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jalajkhandelwal on 05/11/2017.
 */
public class HammingCode {
    public int r;
    public int chunkSize;
    public int parityChunkSize;
    public int paddingSize = 0;
    public int uncorrectedBitCount =0;
    public int uncorrectedChunkCount =0;
    public int totalBitsCorrupted =0;

    public static void main(String[] args) {
        HammingCode h = new HammingCode();
        int length = 800;
        int r =3;
        double p = 0.02;
        List<Boolean> message = h.generateMessage(length);
//        System.out.println("initial message is " + Arrays.toString(message.toArray()));
        List<Boolean> unpaddedMessage = new ArrayList<>();
        unpaddedMessage.addAll(message);

        h.addPadding(message, r);

        ArrayList<Boolean> encodedList = h.encodeMessage(message);
        h.propogateError(encodedList,p);


//        System.out.println("Encoded message is " + Arrays.toString(encodedList.toArray()));
        List<Boolean> lst = h.decodeMessage(encodedList);
//        System.out.println("Final message is   " + Arrays.toString(lst.toArray()));
        h.getUnCorrected(unpaddedMessage,lst);
        System.out.println("Uncorrected Bit Count: " + h.uncorrectedBitCount + " Uncorrected Chunk Count "+ h.uncorrectedChunkCount);
    }

    public void getUnCorrected(List<Boolean> unpaddedMessage, List<Boolean> lst) {
        if (lst.size() != unpaddedMessage.size()){
            System.out.println("ERROR DIFFERENT LENGTHS");
        }

        boolean chunkCount = false;
        for (int i =0 ;i < lst.size();i++){
            if (i% chunkSize == 0){
                if (chunkCount) uncorrectedChunkCount++;
                chunkCount = false;
            }
            if (lst.get(i) != unpaddedMessage.get(i)){
//                System.out.println(i);
                uncorrectedBitCount++;
                if (!chunkCount){
                    chunkCount = true;
                }
            }
        }

    }

    public void propogateError(List<Boolean> message,double error){
        ArrayList<Integer> list = new ArrayList<>();
        Random rand = new Random();
        for(int i=0;i<message.size();i++){
            if (rand.nextDouble() < error){
                message.set(i,!message.get(i));
                list.add(i);
                totalBitsCorrupted++;
            }
        }
//        System.out.println("count is "+totalBitsCorrupted);
//        System.out.println(Arrays.toString(list.toArray()));
    }

    public List<Boolean> decodeMessage(List<Boolean> encodedList) {
        List<List<Boolean>> chunkList = new ArrayList<>();
        for (int i = 0; i < encodedList.size() / parityChunkSize; i++) {
            List<Boolean> t = decodeChunk(encodedList.subList(i * parityChunkSize, (parityChunkSize + i * parityChunkSize)));
            chunkList.add(t);
        }
        List<Boolean> lst = chunkList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return lst.subList(0, lst.size() - paddingSize);
    }

    public ArrayList<Boolean> encodeMessage(List<Boolean> message) {

        List<List<Boolean>> parityMessage = new ArrayList<>();
        for (int i = 0; i < message.size() / chunkSize; i++) {
            parityMessage.add(makeChunk(message.subList(i * chunkSize, (chunkSize + i * chunkSize))));
        }

        List<Boolean> lst = parityMessage.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        ArrayList<Boolean> encodedList = new ArrayList<>();
        encodedList.addAll(lst);
        return encodedList;
    }

    public List<Boolean> generateMessage(int size) {
        Random rand = new Random();

        List<Boolean> message = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Boolean test = rand.nextBoolean();
            message.add(test);
        }
        return message;
    }

    public void addPadding(List<Boolean> message,int r) {
        this.r = r;
        this.chunkSize = ((int) Math.pow(2, r) - r - 1);
        this.parityChunkSize = chunkSize + r;
//        System.out.println(message.size());
        if (message.size() % chunkSize != 0) {
            paddingSize = chunkSize - message.size() % chunkSize;
            int i = 0;
            while (i < paddingSize) {
                message.add(false);
                i++;
            }
        }

    }

    private List<Boolean> decodeChunk(List<Boolean> array) {
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
//            System.out.println(total);
            if ((total - 1) < parityChunkSize) {
                array.set(total - 1, !array.get(total - 1));
            }
        }

        for (int i = 0; i < parityChunkSize; i++) {
            if (Integer.bitCount(i + 1) != 1) {
                retArray.add(array.get(i));
            }
        }
        return retArray;
    }


    private List<Boolean> makeChunk(List<Boolean> array) {
        List<Boolean> messageArray = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < parityChunkSize; i++) {
            if (Integer.bitCount(i + 1) != 1) {
                messageArray.add(array.get(count++));
            } else messageArray.add(null);
        }
        messageArray.toArray();
        insertParity(messageArray);
        return messageArray;
    }

    private void insertParity(List<Boolean> array) {
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
            array.set((1 << i) - 1, !(count % 2 == 0));
        }
    }
}
