package search;

import java.util.Arrays;

public class BinarySearchSpan {
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] arr = Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();


        int start = BinarySearch.iterativeSearch(arr, x);
        // start == arr.length || arr[start] <= x
        if (x == Integer.MIN_VALUE) {
            System.out.println(start + " " + (arr.length - start));
            return;
        }
        int end = BinarySearch.recursiveSearch(arr, x - 1, -1, arr.length);
        // end == arr.length || arr[end] <= (x - 1)
        System.out.println(start + " " + (end - start));
    }
}
