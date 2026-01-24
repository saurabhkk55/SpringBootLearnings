package DSA.Array;

import java.util.HashMap;
import java.util.Map;

public class LongestSubarraySumK {
    static void main() {
        int[] arr = {10, 2, -2, -20, 10};
        int n = arr.length;
        int target = -20;

        Map<Integer, Integer> sumUptoCurrentIdx = new HashMap<>();
        int sum = 0, maxLen = 0;

        for (int idx = 0; idx < n; idx++) {
            sum += arr[idx];

            if (sum == target) maxLen = idx + 1;

            int rem = sum - target;

            if (sumUptoCurrentIdx.containsKey(rem)) {
                int len = idx - sumUptoCurrentIdx.get(rem);
                maxLen = Math.max(len, maxLen);
            }
            if (!sumUptoCurrentIdx.containsKey(sum)) {
                sumUptoCurrentIdx.put(sum, idx);
            }
        }

        System.out.println(maxLen);
    }
}
