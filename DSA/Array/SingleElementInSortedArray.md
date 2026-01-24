540. Single Element in a Sorted Array

```java
class Solution {
     public int singleNonDuplicate(int[] nums) {
     int len = nums.length;
     int low = 0, high = len - 1;
    
     if (len == 1) return nums[0];
    
     while (low <= high) {
        int mid = (low + high) / 2;

        if(mid == 0 && nums[mid] != nums[mid+1]) return nums[mid];
        if (mid == (len - 1) &&  nums[mid] != nums[mid-1]) return nums[mid];
        if (nums[mid] != nums[mid-1] && nums[mid] != nums[mid+1]) return nums[mid];

        if (mid%2 == 0) { // if even
            if (nums[mid] == nums[mid-1]) high = mid - 1;
            else low = mid + 1;
        } else {
            if (nums[mid] == nums[mid-1]) low = mid + 1;
            else high = mid - 1;
        }
     }
     return -1;
     }
}
```