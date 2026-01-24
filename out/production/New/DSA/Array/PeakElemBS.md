162. Find Peak Element

```java
class Solution {
    public int findPeakElement(int[] nums) {
        int len = nums.length;

        if (len == 1) return 0;

        int low = 0, high = len - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (mid == 0 && nums[mid] > nums[mid+1]) return mid;
            else if (mid == len - 1 && nums[mid] > nums[mid-1]) return mid;
            else if ((mid == 0 || nums[mid] > nums[mid-1]) && (mid == len - 1 || nums[mid] > nums[mid+1])) return mid;

            else if ((mid == 0 || nums[mid] > nums[mid-1]) && (mid == len - 1 || nums[mid] < nums[mid+1])) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }
}
```
