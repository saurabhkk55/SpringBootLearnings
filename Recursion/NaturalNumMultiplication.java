package Recursion;

public class NaturalNumMultiplication {
    static void main() {

        int n = 3;

        int product = multiplicationOfNaturalNum(n);

        System.out.println(product);
    }

    static int multiplicationOfNaturalNum(int num) {

        if(num == 1) {
            return 1;
        }

        return num * multiplicationOfNaturalNum(num - 1);
    }
}
