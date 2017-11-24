import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jalajkhandelwal on 23/11/2017.
 */
public class StatisticsTests {
    static int length = 500;
    static double prob = 0.05;
    static int rVal = 3;
    static int repeats = 10000;
    public static void main(String[] args) {
        try {

//           length();
            prob();
//            r();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void length() throws Exception{
        BufferedWriter bwLength = new BufferedWriter(new FileWriter("stats/lengthvar" + repeats + ".csv", false));
        for (int j = 10; j < 1010; j += 10) {
            int uncorBitC = 0;
            int uncorC = 0;
            int totalCorrup = 0;
            for (int counter = 0; counter < repeats; counter++) {
                HammingCode h = new HammingCode();
                List<Boolean> message = h.generateMessage(j);
                List<Boolean> unpaddedMessage = new ArrayList<>();
                unpaddedMessage.addAll(message);
                h.addPadding(message, rVal);
                ArrayList<Boolean> encodedList = h.encodeMessage(message);
                h.propogateError(encodedList, prob);
                List<Boolean> lst = h.decodeMessage(encodedList);
                h.getUnCorrected(unpaddedMessage, lst);
                uncorBitC += h.uncorrectedBitCount;
                uncorC += h.uncorrectedChunkCount;
                totalCorrup += h.totalBitsCorrupted;
            }
            int avguncorBitC = uncorBitC / repeats;
            int avguncorC = uncorC / repeats;
            int avgcorB = totalCorrup / repeats;
            bwLength.write(j + "," + avgcorB + "," + avguncorBitC + "," + avguncorC);
            bwLength.newLine();
        }
        bwLength.close();
        System.out.println("length finished");
    }

    public static void prob() throws Exception{
        BufferedWriter bwProb = new BufferedWriter(new FileWriter("data/probvar" + repeats + ".csv", false));
        for (double p = 0.05; p < 0.55; p += 0.05) {
            double probability =  Math.round((p*100.0))/100.0;
            System.out.println(probability);
            int uncorBitC = 0;
            int uncorC = 0;
            int totalCorrup = 0;
            for (int counter = 0; counter < repeats; counter++) {
                HammingCode h = new HammingCode();

                List<Boolean> message = h.generateMessage(length);
                List<Boolean> unpaddedMessage = new ArrayList<>();
                unpaddedMessage.addAll(message);
                h.addPadding(message, rVal);
                ArrayList<Boolean> encodedList = h.encodeMessage(message);
                h.propogateError(encodedList, probability);
                List<Boolean> lst = h.decodeMessage(encodedList);
                h.getUnCorrected(unpaddedMessage, lst);
                uncorBitC += h.uncorrectedBitCount;
                uncorC += h.uncorrectedChunkCount;
                totalCorrup += h.totalBitsCorrupted;
            }
            int avguncorBitC = uncorBitC / repeats;
            int avguncorC = uncorC / repeats;
            int avgcorB = totalCorrup / repeats;
            bwProb.write(probability + "," + avgcorB + "," + avguncorBitC + "," + avguncorC);
            bwProb.newLine();
        }
        bwProb.close();

        System.out.println("probability finished");
    }


    public static void r() throws Exception{



        BufferedWriter bwR = new BufferedWriter(new FileWriter("data/rvar" + repeats + ".csv", false));
        for (int r = 2; r < 7; r++) {
            int uncorBitC = 0;
            int uncorC = 0;
            int totalCorrup = 0;
            for (int counter = 0; counter < repeats; counter++) {
                HammingCode h = new HammingCode();

                List<Boolean> message = h.generateMessage(length);
                List<Boolean> unpaddedMessage = new ArrayList<>();
                unpaddedMessage.addAll(message);
                h.addPadding(message, r);
                ArrayList<Boolean> encodedList = h.encodeMessage(message);
                h.propogateError(encodedList, prob);
                List<Boolean> lst = h.decodeMessage(encodedList);
                h.getUnCorrected(unpaddedMessage, lst);
                uncorBitC += h.uncorrectedBitCount;
                uncorC += h.uncorrectedChunkCount;
                totalCorrup += h.totalBitsCorrupted;
            }
            int avguncorBitC = uncorBitC / repeats;
            int avguncorC = uncorC / repeats;
            int avgcorB = totalCorrup / repeats;
            bwR.write(r + "," + avgcorB + "," + avguncorBitC + "," + avguncorC);
            bwR.newLine();
        }

        bwR.close();
        System.out.println("r finished");
    }
}
