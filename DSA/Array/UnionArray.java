package DSA.Array;

import java.util.ArrayList;
import java.util.List;

public class UnionArray {
    static void main() {
        int[] nums1 = {1, 2, 3, 4, 5};
        int[] nums2 = {1, 2, 7};

        int len1 = nums1.length, len2 = nums2.length, i = 0, j = 0;

        List<Integer> ans = new ArrayList<>();

        while (i < len1 && j < len2) {
            if (nums1[i] <= nums2[j]) {
                if (ans.isEmpty() || ans.get(ans.size()-1) != nums1[i]) ans.add(nums1[i]);
                i++;
            } else if (nums2[j] < nums1[i]) {
                if (ans.isEmpty() || ans.get(ans.size()-1) != nums2[j]) ans.add(nums2[j]);
                j++;
            }
        }

        while (i < len1) {
            if (ans.isEmpty() || ans.get(ans.size()-1) != nums1[i]) {
                ans.add(nums1[i]);
            }
            i++;
        }

        while (j < len2) {
            if (ans.isEmpty() || ans.get(ans.size()-1) != nums2[j]) {
                ans.add(nums2[j]);
            }
            j++;
        }

        int[] res = new int[ans.size()];

        for (int idx = 0; idx < ans.size(); idx++) {
            res[idx] = ans.get(idx);
        }

        for (int re : res) {
            System.out.println(re);
        }
    }
}
