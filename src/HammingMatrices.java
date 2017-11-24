import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * R
 * Created by jalajkhandelwal on 14/11/2017.
 */
public class HammingMatrices {
    private int r;

    public static void main(String[] args) {
        HammingMatrices hm = new HammingMatrices();
        int r;
        int counter =0;
        int length = 500;

//        for (int l =0;l < 1000000;l++){
        for (int k = 2; k < 7;k++ ) {
            r = k;
//        for (int i = 2; i < 7;i++ ){
            int[][] G = hm.generateG(r);
            int[][] H = hm.generateH(G);
            HashMap<String, String> syndromeTable = hm.generateSyndromeTable(H);
//            printMatrix(G);
//            System.out.println();
//            printMatrix(H);
//            System.out.println();
//            hm.printSyndromeTable(syndromeTable);
            int chunkSize = (int) Math.pow(2, r) - r - 1;
            int[] testy = new int[chunkSize];
            Random rand = new Random();
            for (int i = 0; i < testy.length; i++) {
                testy[i] = (int) rand.nextInt(1);
                System.out.println(testy[i]);
            }
//            System.out.println(Arrays.toString(testy));
            int[] newArray = hm.multiplyMatrices(testy, G);
//            System.out.println(Arrays.toString(newArray));
//            System.out.println(Arrays.toString(otherArray));
            for (int j = 0; j < chunkSize + r; j++) {
                newArray[j] = newArray[j] ^ 1;
                int[] parityBit = hm.multiplyMatrices(newArray, H);
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < parityBit.length; i++) {
                    s.append(parityBit[i]);
                }

                String t = syndromeTable.get(s.toString());
                for (int i = 0; i < t.length(); i++) {
                    if (t.charAt(i) == '1') {
//                        System.out.println(i);
                        newArray[i] = newArray[i] ^ 1;
                    }
                }
                int[] finalAr = new int[chunkSize];
                System.arraycopy(newArray, 0, finalAr, 0, finalAr.length);

                if (Arrays.equals(finalAr, testy)) {
//                    System.out.println("HUZZAH");
                    counter++;
                } else {
                    System.out.println("BROKE HERE " + j + " " + r);
                    System.exit(0);
                }
            }

//        }
        }
        System.out.println("DONE with count "+counter);
    }

    private void printSyndromeTable(HashMap<String, String> syndromeTable) {
        for (HashMap.Entry<String, String> s : syndromeTable.entrySet()) {
            System.out.println(s.getKey() + " " + s.getValue());
        }
    }

    private int[][] generateG(int MatrixR) {
        this.r = MatrixR;
        int codeSize = (int) Math.pow(2, r) - r - 1;
        int[][] g = new int[codeSize][codeSize + r];
        String startnum;
        int count = 1;
        for (int i = 0; i < g.length; i++) {
            while (Integer.bitCount(count) == 1) {
                count++;
            }
            String s = Integer.toBinaryString(count);
            count++;
            while (s.length() < r) {
                s = "0" + s;
            }
            for (int j = 0; j < r; j++) {
                int value = Integer.valueOf(String.valueOf(s.charAt(j)));
                g[i][g[0].length - r + j] = value;
            }
        }

        int num = 0;
        for (int i = 0; i < codeSize; i++) {
            g[i][num++] = 1;
        }

        return g;
    }

    private int[] chunkCode(int[] a,int start, int end){
        return Arrays.copyOfRange(a,start,end);
    }

    private int[][] generateH(int[][] G) {
        int codeSize = (int) Math.pow(2, r) - r - 1;
        int[][] h = new int[codeSize + r][r];
        int count = 0;
        for (int i = 0; i < h.length; i++) {
            if (i < G.length) {
                for (int j = 0; j < h[0].length; j++) {
                    h[i][j] = G[i][codeSize + j];
                }
            } else {
                h[i][count++] = 1;
            }
        }
        return h;
    }


    private int[] multiplyMatrices(int[] m1, int[][] m2) {
        int[] retArray = new int[m2[0].length];
        for (int i = 0; i < m2[0].length; i++) {
            int total = 0;
            for (int j = 0; j < m2.length; j++) {
                total += m1[j] * m2[j][i];
                total %= 2;
            }
//                System.out.println(total);
            retArray[i] = total;
        }
        return retArray;
    }


    private HashMap<String, String> generateSyndromeTable(int[][] H) {
        HashMap<String, String> map = new HashMap<>();
        int[] syndrome = new int[(int) Math.pow(2, r) - 1];
        for (int i = 0; i < syndrome.length + 1; i++) {

            if (i != 0) syndrome[syndrome.length - i] = 1;
            StringBuilder t = new StringBuilder();
            for (int j = 0; j < syndrome.length; j++) {
                t.append(syndrome[j]);
            }
//            System.out.println(t);
            int[] total = multiplyMatrices(syndrome, H);
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < total.length; j++) {
                s.append(total[j]);
            }
//            System.out.println(s);
            if (i != 0) syndrome[syndrome.length - i] = 0;
            map.put(s.toString(), t.toString());
        }
        return map;
    }


    private static void printMatrix(int[][] parityCheck) {
        for (int i = 0; i < parityCheck.length; i++) {
            System.out.print("[");
            for (int j = 0; j < parityCheck[0].length; j++) {
                if (j == parityCheck[0].length - 1) {
                    System.out.print(parityCheck[i][j] + "]\n");
                } else {
                    System.out.print(parityCheck[i][j] + " ");
                }
            }
        }
    }

}

