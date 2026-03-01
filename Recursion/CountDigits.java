package Recursion;

public class CountDigits {

    static int count = 0;

    static void countDigits(int num) {

        if(num == 0) return;

        countDigits(num / 10);

        count++;
    }

    static void main() {

        int n = 12000;

        countDigits(n);

        System.out.println(count);
    }
}
