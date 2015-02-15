package primes;

import java.util.BitSet;

public class SegmentedPrimeSieve {

    public static BitSet computePrimes(int limit)
    {
        final BitSet primes = new BitSet();
        primes.set(0, false);
        primes.set(1, false);
        primes.set(2, limit, true);
        for (int d = 2; d < (long)Math.ceil(Math.sqrt(limit)); d++)
        {
            if (primes.get(d))
            {
                for (int m = d * d; m < limit; m += d)
                {
                    primes.clear(m);
                }
            }
        }
        return primes;
    }

    public static BitSet computePrimes(long start, long limit)
    {
        if (limit - start > Integer.MAX_VALUE)
            throw new IllegalArgumentException();

        final long sqrtLimit = (long)Math.ceil(Math.sqrt(limit));
        final BitSet primes = computePrimes( (int)sqrtLimit);

        final BitSet segment = new BitSet();
        if (0 - start >= 0)
            segment.set((int) (0 - start), false);
        if (1 - start >= 0)
            segment.set((int) (1 - start), false);
        segment.set((int) (Math.max(0, 2 - start)), (int) (limit - start), true);
        for (int d = 2; d < sqrtLimit; d++)
        {
            if (primes.get(d))
            {
                final int remainder = (int) (start % d);
                final long mStart = start - remainder + (remainder == 0 ? 0 : d);
                for (long m = Math.max(mStart, d * d); m < limit; m += d)
                    segment.clear((int) (m - start));
            }
        }
        return segment;
    }
}
