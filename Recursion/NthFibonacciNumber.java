package Recursion;

public class NthFibonacciNumber {
    static void main() {

        int n = 6;

        int num = findNthFibonacciNum(n-1);

        System.out.println(num);
    }

    static int findNthFibonacciNum(int num) {

        if(num == 0) return 0;
        if(num == 1) return 1;

        int n1 = findNthFibonacciNum(num - 1);
        int n2 = findNthFibonacciNum(num - 2);

        return n1 + n2;
    }
}
