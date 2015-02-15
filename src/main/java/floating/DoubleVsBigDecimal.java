package floating;


import java.math.BigDecimal;
import java.math.MathContext;

public class DoubleVsBigDecimal {

    public static void main(String[] args) {
        long t1,t2 = System.currentTimeMillis();

        t1 = System.currentTimeMillis();
        int ITERS = 100_000_000;
        int res = 0;
        final BigDecimal orig = new BigDecimal( "362.2" );
        final BigDecimal mult = new BigDecimal( "0.015" ); //1.5%
        for ( int i = 0; i < ITERS; ++i )
        {
            final BigDecimal result = orig.multiply( mult, MathContext.DECIMAL64 );
            if ( result != null ) res++;
        }
        t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

        t1 = System.currentTimeMillis();
        final double orig2 = 36220; //362.2 in cents
        for ( long i = 0; i < ITERS; ++i ) {
            final long result = Math.round( orig2 * i );
            if ( result != 543 ) res++;    //compare with something
        }
        t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

    }

}
