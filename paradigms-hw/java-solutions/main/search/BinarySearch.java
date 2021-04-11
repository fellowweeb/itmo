package search;

import java.util.Arrays;

public class BinarySearch {
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] arr = Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();

        System.out.println(iterativeSearch(arr, x));
//        System.out.println(recursiveSearch(arr, x, -1, arr.length));
    }

    //pre: (forall arr[i+1] <= arr[i]) && l < r && (arr[l] > x || l == -1) && (r == arr.length || arr[r] <= x)
    //post: R == arr.length || arr[R] <= x
    public static int recursiveSearch(int[] arr, int x, int l, int r) {
        if (r - l == 1) {
            // r - l == 1 && (arr[l] > x || l == -1) && (r == arr.length || arr[r] <= x)
            return r;
        }
        // -1 <= l < r <= arr.length
        int m = (l + r) / 2;
        // l < m < r
        // 0 <= m < arr.length
        if (arr[m] > x) {
            // arr[m] > x
            l = m;
            // l == m && arr[l] > x && l < r
        } else {
            // arr[m] <= x
            r = m;
            // r == m && arr[r] <= x && r > l
        }
        // r > l && r - l != 1 && (arr[l] > x || l == -1) && (r == arr.length || arr[r] <= x)
        return recursiveSearch(arr, x, l, r);
    }

    //pre: forall arr[i+1] <= arr[i]
    //post: R == arr.length || arr[R] <= x
    public static int iterativeSearch(int[] arr, int x) {
        int l = -1, r = arr.length, m;
        // l ==-1 && r == arr.length

        // r > l && (arr[l] > x || l == -1) && (arr[r] <= x || r == arr.length)
        while (r - l != 1) {
            m = (l + r) / 2;
            // l < m < r

            // 0 <= m < arr.length
            if (arr[m] > x) {
                // arr[m] > x
                l = m;
                // l == m && arr[l] > x && l < r
            } else {
                // arr[m] <= x
                r = m;
                //r == m && arr[r] <= x && r > l
            }
        }
        // r - l == 1 && (arr[l] > x || l == -1) && (arr[r] <= x || r == arr.length)
        return r;
    }
}
