package Recursion;

public class NaturalNum {
    static void main() {

        int n = 5;

        printNaturalNum(n);

        printNaturalNumInReverseOrder(n);
    }

    static void printNaturalNum(int num) {

        if(num == 1) {

            System.out.println(num + " ");
            return;
        }

        printNaturalNum(num - 1);
        System.out.println(num + " ");
    }

    static void printNaturalNumInReverseOrder(int num) {

        if(num == 1) {

            System.out.println(num + " ");
            return;
        }

        System.out.println(num + " ");
        printNaturalNum(num - 1);
    }
}
