package DSA.Array;

public class MaximumConsecutiveOnes {
    static void main() {
        int[] nums = {1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1};    // OUTPUT: 4
        int[] nums1 = {0, 0, 0};    // OUTPUT: 4

        int arrLength = nums.length;

        int leftIdx = 0, rightIdx = 0, counter = 0, maxCounter = 0;

        while (leftIdx < arrLength && rightIdx < arrLength) {
            if (nums[leftIdx] == 1 && nums[rightIdx] == 1) {
                counter++;
                maxCounter = Math.max(counter, maxCounter);
                rightIdx++;
            } else {
                leftIdx = rightIdx + 1;
                counter = 0;
                rightIdx++;
            }
        }

        System.out.println("Maximum Consecutive One's in the given array: " + maxCounter);
    }
}
