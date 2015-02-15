package primes;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class PrimeChecker {

    static boolean isPrime1(long n) {
        if (n%2==0) return false;
        long end = (long)Math.sqrt(n);
        for(long i=3;i<=end;i+=2) {
            if(n%i==0) {
                System.out.println(i);
                return false;
            }
        }
        return true;
    }


    static boolean isPrime2(long n) {
        if (n <= 3) {
            return n > 1;
        } else if (n % 2 == 0 || n % 3 == 0) {
            return false;
        } else {
            double sqrtN = Math.floor(Math.sqrt(n));
            for (long i = 5; i <= sqrtN; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    static boolean isPrime3(long n) {
        return BigInteger.valueOf(n).isProbablePrime(100);
    }

    public static void main(String[] args) throws Exception {
//        BufferedWriter out = new BufferedWriter(new FileWriter("/home/ccreanga/tmp/primes.txt"));

        long l = Math.abs(BigInteger.probablePrime(63, new SecureRandom()).longValue());
        //l = Long.MAX_VALUE-24;
        System.out.println(l);


        long t1 = System.currentTimeMillis();
//        System.out.println(isPrime1(l));
        long t2 = System.currentTimeMillis();

        System.out.println((t2-t1)/1_000);
        t1 = System.currentTimeMillis();
//        System.out.println(isPrime2(l));
        t2 = System.currentTimeMillis();
        System.out.println((t2-t1)/1_000);

        t1 = System.currentTimeMillis();
        System.out.println(isPrime3(l));
        t2 = System.currentTimeMillis();
        System.out.println((t2-t1)/1_000);

//        for(long i=0;i<Long.MAX_VALUE;i++){
//            if (isPrime(i)){
//                out.write("" + i);
//                out.newLine();
//                if (i>100_000_000)
//                System.out.println("" + i);
//            }
//        }
//        out.close();
    }

}
