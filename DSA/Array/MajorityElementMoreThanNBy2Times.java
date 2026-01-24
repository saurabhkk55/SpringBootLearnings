package DSA.Array;

public class MajorityElementMoreThanNBy2Times {
    static void main() {
        int[] nums = {2, 2, 1, 1, 1, 2, 2, 1, 1}; // OUTPUT: 2

        int counter = 0, targetElem = -1;

        for (int num : nums) {
            if(counter == 0 || num == targetElem) {
                counter++;
                targetElem = num;
            } else {
                counter--;
            }
        }

        if (counter != 0) {
            System.out.println("Elem that occurs more than N/2 times: " + targetElem);
        }
    }
}
