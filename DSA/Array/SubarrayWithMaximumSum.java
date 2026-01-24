package DSA.Array;

public class SubarrayWithMaximumSum {
    static void main() {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        int sum = 0, maxSum = Integer.MIN_VALUE;
        int tempSubArrStartIdx = 0, subArrStartIdx = 0, subArrEndIdx = 0;

        for (int idx = 0; idx < arr.length; idx++) {
            sum += arr[idx];

            if (sum > maxSum) {
                maxSum = sum;
                subArrStartIdx = tempSubArrStartIdx;
                subArrEndIdx = idx;
            }

            if (sum < 0) {
                sum = 0;
                tempSubArrStartIdx = idx + 1;
            }
        }

        System.out.println("Subarray with the Maximum Sum");
        System.out.println(" - Start index: " + subArrStartIdx);
        System.out.println(" - End index: " + subArrEndIdx);
    }
}
