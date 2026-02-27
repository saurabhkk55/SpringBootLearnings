package Recursion;

public class PowerOfNum {
    static void main() {

        int n = 2, power = 4;

        int ans = nthPowerOfNum(n, power);

        System.out.println(ans);
    }

    static int nthPowerOfNum(int num, int power) {

        if(power == 0) return 1;
        if(power == 1) return num;

        int n1 = nthPowerOfNum(num, power-1);
        int n2 = num;

        return n1*n2;
    }
}
