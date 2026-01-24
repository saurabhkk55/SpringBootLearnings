package DSA.Array;

import javax.lang.model.element.Element;

public class LargestElement {
    static void main() {

        // 1. Largest Element
        int[] array = { 12, 35, 1, 10, 34, 1 };

        int maxElem = 0;

        for (int elem : array) {
            if(elem > maxElem) maxElem = elem;
        }

        System.out.println("Largest element: " + maxElem);

        // 2. Second Largest Element
        int[] arr = { 12, 35, 1, 10, 34, 1 };

        int largest = Integer.MIN_VALUE, secondLargest = Integer.MIN_VALUE;

        for (int elem : array) {
            if (elem > largest) {
                secondLargest = largest;
                largest = elem;
            } else if (elem > secondLargest && elem != largest) {
                secondLargest = elem;
            }
        }

        // System.out.println("Largest element: " + largest);
        System.out.println("\nSecond largest element: " + secondLargest);

        // 3. Smallest Element
        int[] arr1 = { 12, 35, 1, 10, 34, 1 };

        int smallestElem = Integer.MAX_VALUE;

        for (int elem : array) {
            if(elem < smallestElem) smallestElem = elem;
        }

        System.out.println("\nSmallest element: " + smallestElem);

        // 4. Second Smallest Element
        int[] arr2 = { 12, 35, 1, 10, 34, 1 };

        int smallest = Integer.MAX_VALUE, secondSmallest = Integer.MAX_VALUE;

        for (int elem : arr2) {
            if(elem < smallest) {
                secondSmallest = smallest;
                smallest = elem;
            } else if (elem < secondSmallest && elem != smallest) {
                secondSmallest = elem;
            }
        }

        System.out.println("\nSecond smallest element: " + secondSmallest);

        // 5. Check if an Array is Sorted
        int[] arr3 = { 1, 2, 3, 4, 5 };
        int[] arr4 = { 7, 2, 8, 5, 3 };

        int mini = Integer.MIN_VALUE;
        boolean sorted = true; // assuming given array is already sorted

        for (int elem : arr4) {
            if(elem > mini) mini = elem;
            else sorted = false; // when array is not sorted
        }

        System.out.println("\nIs given array sorted? " + (sorted ? "Yes" : "No"));

        // 6. Remove duplicates from a sorted array in-place
        // - Given a sorted array containing duplicate elements.
        // - Remove duplicates in-place (modify the original array) and return the number of unique elements.
        // - The first k elements of the array should hold the unique elements after modification.
        // - It does not matter what you leave beyond the first k elements.

        int[] arr5 = { 1, 1, 2, 2, 3, 4, 4, 4, 5 };
        int[] arr6 = { 1, 1, 1, 1, 1 };
        int[] arr7 = { 1, 2, 3, 4, 5 };

        int left = 0, right = 0;

        while (left < arr5.length && right < arr5.length) {
            if(arr5[left] == arr5[right]) right++;
            else {
                if(left + 1 != right) {
                    // swap arr[left+1] with arr[right]
                    arr5[left + 1] = arr5[left + 1] ^ arr5[right];
                    arr5[right] = arr5[left+1] ^ arr5[right];
                    arr5[left + 1] = arr5[left+1] ^ arr5[right];
                }
                left++;
                right++;
            }
        }

        System.out.println("\nUnique array size: " + (left + 1));

        for (int elem : arr5) {
            System.out.print(elem + " ");
        }

        // 7. Left Rotate the Array by One
        int[] arr8 = { 1, 2, 3, 4, 5 }; // OUTPUT: [2, 3, 4, 5, 1]

        int arrLength = arr8.length;
        int leftMostElem = arr8[0];

        for (int idx = 1; idx < arrLength; idx++) {
            arr8[idx - 1] = arr8[idx];
        }

        arr8[arrLength - 1] = leftMostElem;

        System.out.print("\n\nGiven Array is left rotated by one: ");
        for (int elem : arr8) {
            System.out.print(elem + " ");
        }
    }
}
