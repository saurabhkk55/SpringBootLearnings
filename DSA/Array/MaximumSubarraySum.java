package DSA.Array;

public class MaximumSubarraySum {
    static void main() {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        int sum = 0, maxSum = Integer.MIN_VALUE;

        for (int idx = 0; idx < arr.length; idx++) {
            sum += arr[idx];

            maxSum = Math.max(sum, maxSum);

            if (sum < 0) {
                sum = 0;
            }
        }

        System.out.println("Maximum Subarray Sum (Kadane's Algo): " + maxSum);
    }
}
