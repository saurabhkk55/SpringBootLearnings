package Recursion;

public class SumDigits {

    static int sum = 0;

    static void sumDigits(int num) {

        if(num == 0) return;

        sum += num % 10;

        sumDigits(num / 10);
    }


    static void main() {

        int n = 12200;

        sumDigits(n);

        System.out.println(sum);
    }
}
