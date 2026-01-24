package DSA.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FourSum {
    static void main() {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        int target = 0;

        Arrays.sort(nums); // {-4, -1, -1, 0, 1, 2};

        int a = 0, i, j, k, len = nums.length;
        List<List<Integer>> answers = new ArrayList<>();

        while (a < len) {
            while ((a < len) && (a != 0) && (nums[a] == nums[a-1])) {
                a++;
            }

            i = a + 1;

            while (i < len) {
                while ((i < len) && (i != 0) && (i-1 != a) && (nums[i] == nums[i-1])) {
                    i++;
                }

                j = i + 1;
                k = len - 1;

                while (j < k) {
                    int fourSum = nums[a] + nums[i] + nums[j] + nums[k];

                    if (fourSum == target) {
                        List<Integer> subAnswer = new ArrayList<>();
                        subAnswer.add(nums[a]);
                        subAnswer.add(nums[i]);
                        subAnswer.add(nums[j]);
                        subAnswer.add(nums[k]);

                        answers.add(subAnswer);

                        j++;
                        k--;

                        while (j < k && nums[j] == nums[j-1]) {
                            j++;
                        }

                        while (k > j && nums[k] == nums[k+1]) {
                            k--;
                        }
                    } else if (fourSum < target) {
                        j++;
                    } else {
                        k--;
                    }
                }
                i++;
            }
            a++;
        }

        for (List<Integer> answer : answers) {
            System.out.println(answer);
        }
    }
}
