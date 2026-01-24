package DSA.Array;

public class MoveAllZeroesToEnd {
    static void main() {
        int[] arr = { 0, 1, 0, 3, 12 }; // OUTPUT: [1, 3, 12, 0, 0]
        int[] arr1 = { 1, 0, 0, 2, 0, 5, 9 }; // OUTPUT: [1, 2, 5, 9, 0, 0, 0]
        int[] arr2 = { 0, 0, 0, 2, 9 }; // OUTPUT: [2, 9, 0, 0, 0]

        int arrlength = arr.length;

        int leftIdx = 0, rightIdx = 0;

        while (leftIdx < arrlength && rightIdx < arrlength) {
            if(arr[leftIdx] == 0 && arr[rightIdx] == 0) rightIdx++;
            else if (arr[leftIdx] == 0 && arr[rightIdx] != 0) {
                // swap
                arr[leftIdx] = arr[leftIdx] ^ arr[rightIdx];
                arr[rightIdx]= arr[leftIdx] ^ arr[rightIdx];
                arr[leftIdx] = arr[leftIdx] ^ arr[rightIdx];

                leftIdx++; rightIdx++;
            } else if (arr[leftIdx] != 0 && arr[rightIdx] != 0) {
                leftIdx++; rightIdx++;
            }
        }

        for (int elem : arr) {
            System.out.print(elem + " ");
        }
    }
}
