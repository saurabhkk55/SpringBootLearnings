package Recursion;

public class Factorial {
    static void main() {

        int n = 0;

        int factorial = findFactorial(n);

        System.out.println(factorial);
    }

    static int findFactorial(int num) {

        if(num == 0 || num == 1) return 1;

        int n1 = num;
        int n2 = findFactorial(num - 1);

        return n1 * n2;
    }
}
