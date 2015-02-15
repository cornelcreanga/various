package primes;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class Sieve {


    static long debut = 1;
    static long fin = 10000;
    private static byte[] segment = new byte[(int)(fin-debut)];

    public static void main(String[] args) {


        Arrays.fill(segment, (byte) 0);

        long finSqrt = (long) Math.sqrt(fin) + 1;

        int p = -1;
        int i = 1;
        while (p < finSqrt) {
            p = segment[i++];
            long j = p - (debut % p);
            j += ((j & 1) == 0 ? p : 0);

            long p2 = p * 2;
            while (j < fin) {
                segment[(int) (j >> 4)] |= (1 << ((j >> 1) & 7));
                j += p2;
            }
        }


    }

}
