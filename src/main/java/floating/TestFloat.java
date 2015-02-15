package floating;

public class TestFloat {

    public static void main(String[] args) {

        //http://en.wikipedia.org/wiki/IEEE_floating_point
        //http://en.wikipedia.org/wiki/IEEE_754-1985

        double d = 0.49999999999999993;
        System.out.println((long)Math.floor(d + 0.5d));
        System.out.println(round(d));
        System.out.println(444444444444444444444444444444d);
        System.out.println(0x1.0p-1);
        System.out.println(0x1.fffffffffffffp-2);

        long binary = Double.doubleToLongBits(4);

        String strBinary = Long.toBinaryString(binary);
        System.out.println(Double.toHexString(0.49999999999999999));
        System.out.println(Double.toHexString(0.49999999999999998));
        System.out.println(Double.toHexString(0.49999999999999997));
        System.out.println(Double.toHexString(0.49999999999999996));
        System.out.println(Double.toHexString(0.49999999999999995));
        System.out.println(Double.toHexString(0.49999999999999994));
        System.out.println(Double.toHexString(0.49999999999999993));
        System.out.println(Double.toHexString(0.49999999999999992));
        System.out.println(Double.toHexString(0.49999999999999991));
        System.out.println(Double.toHexString(0.00000000000000001));
        System.out.println(Double.toHexString(0.00000000000000002));
        System.out.println(Double.toHexString(2));

    }

    public static long round(double  a) {
        if (a != 0x1.fffffffffffffp-2) // greatest double value less than 0.5
            return (long)Math.floor(a + 0.5d);
        else
            return 0;
    }

}
