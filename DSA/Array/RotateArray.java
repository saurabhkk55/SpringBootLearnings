package DSA.Array;

public class RotateArray {
    static void main() {
        int[] array = { 1, 2, 3, 4, 5, 6, 7 }; // OUTPUT: [3, 4, 5, 6, 7, 1, 2]
        int k = 2;

        reverseSubArray(array, 0, k-1);
        reverseSubArray(array, k, array.length - 1);
        reverseSubArray(array, 0, array.length - 1);

        for (int elem : array) {
            System.out.print(elem + " ");
        }
    }

    static void reverseSubArray(int[] arr, int startIdx, int endIdx) {
        while (startIdx < endIdx) {
            arr[startIdx] = arr[startIdx] ^ arr[endIdx];
            arr[endIdx] = arr[startIdx] ^ arr[endIdx];
            arr[startIdx] = arr[startIdx] ^ arr[endIdx];

            startIdx++;
            endIdx--;
        }
    }
}
