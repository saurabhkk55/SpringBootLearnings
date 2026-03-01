package Recursion;

public class ReverseNumber {

    static int reverseNum = 0;

    static void reverseNum(int num) {

        if(num == 0) return;

        reverseNum = reverseNum * 10 + (num % 10);

        reverseNum(num / 10);
    }

    static void main() {

        int n = 12;

        reverseNum(n);

        System.out.println(reverseNum);
    }
}
