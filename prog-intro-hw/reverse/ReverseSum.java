package reverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReverseSum {
    private static final int MIN_ARRAY_SIZE = 8;

    public static void main(String[] args) {
        List<int[]> matrix = new ArrayList<>();
        Scanner scanStr = new Scanner(System.in);
        int maxStrLen = 0;
        while (scanStr.hasNextLine()) {
            matrix.add(new int[MIN_ARRAY_SIZE]);
            Scanner scanInt = new Scanner(scanStr.nextLine());
            int currLen = 0;
            int[] arr = matrix.get(matrix.size() - 1);
            while (scanInt.hasNextInt()) {
                if (currLen == arr.length) {
                    arr = Arrays.copyOf(arr, arr.length * 2);
                    matrix.set(matrix.size() - 1, arr);
                }
                arr[currLen++] = scanInt.nextInt();
            }
            arr = Arrays.copyOf(arr, currLen);
            matrix.set(matrix.size() - 1, arr);
            maxStrLen = Integer.max(maxStrLen, currLen);
            scanInt.close();
        }
        scanStr.close();
        int[] strCnt = new int[matrix.size()];
        int[] colCnt = new int[maxStrLen];
        for (int i = 0; i < strCnt.length; i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                strCnt[i] += matrix.get(i)[j];
                colCnt[j] += matrix.get(i)[j];
            }
        }
        for (int i = 0; i < strCnt.length; i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                System.out.print(strCnt[i] + colCnt[j] - matrix.get(i)[j] + " ");
            }
            System.out.println();
        }
    }
}
