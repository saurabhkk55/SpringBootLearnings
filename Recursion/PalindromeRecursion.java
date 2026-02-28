package Recursion;

public class PalindromeRecursion {

    static int reverse = 0;  // global variable

    public static boolean isPalindrome(int number) {

        // Base condition
        if (number == 0) {
            return true;
        }

        int lastDigit = number % 10;
        reverse = reverse * 10 + lastDigit;

        isPalindrome(number / 10);

        System.out.println("reverse: " + reverse);
        System.out.println("number: " + number);
        System.out.println((reverse == number) + "----------");

        return reverse == number;
    }

    public static void main(String[] args) {
        int num = 121;

        reverse = 0;  // reset before calling
        if (isPalindrome(num)) {
            System.out.println("Palindrome");
        } else {
            System.out.println("Not Palindrome");
        }
    }
}
