package Recursion;

public class NaturalNUmSum {
    static void main() {
        int n = 3;

        int sum = sumOfNaturalNum(n);

        System.out.println(sum);
    }

    static int sumOfNaturalNum(int num) {
        if(num == 1) {
            return 1;
        }

        return num + sumOfNaturalNum(num - 1);
    }
}
