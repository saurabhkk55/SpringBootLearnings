package DSA.Array;

import java.util.ArrayList;
import java.util.List;

public class MergeIntervals {
    static void main() {
       int[][] intervals = {{1,3},{2,6},{8,10},{15,18}};

        System.out.println(intervals.length);

        List<List<Integer>> answer = new ArrayList<>();

        int[] interval = intervals[0];

        List<Integer> temp = new ArrayList<>();
        temp.add(intervals[0][0]);
        temp.add(intervals[0][1]);

        answer.add(temp);

        for (int i = 1; i < intervals.length; i++) {
            int lastIdx = answer.size() - 1;
            int prevStart = answer.get(lastIdx).get(0);
            int prevEnd = answer.get(lastIdx).get(1);
            int currStart = intervals[i][0];
            int currEnd = intervals[i][1];

            if (prevEnd >= currStart) {
                if (prevEnd < currEnd) {
                    answer.get(lastIdx).set(1, currEnd);
                }
            } else {
                List<Integer> newTemp = new ArrayList<>();
                newTemp.add(currStart);
                newTemp.add(currEnd);

                answer.add(newTemp);
            }
        }

        int[][] sol = new int[answer.size()][2];

        for (int i = 0; i < answer.size(); i++) {
            sol[i][0] = answer.get(0).get(0);
            sol[i][1] = answer.get(0).get(1);
        }

        for (int[] mergeIntervals : sol) {
            System.out.print(mergeIntervals + " ");
        }
    }
}
