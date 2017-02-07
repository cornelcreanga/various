package floating;

public class SubnormalNumbers {
    public static void main(String[] args) {

        long t1 = System.currentTimeMillis();

        float[] x = new float[]{1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2.0f, 2.1f, 2.2f, 2.3f, 2.4f, 2.5f, 2.6f};
        float[] z = new float[]{1.123f, 1.234f, 1.345f, 156.467f, 1.578f, 1.689f, 1.790f, 1.812f, 1.923f, 2.034f, 2.145f, 2.256f, 2.367f, 2.478f, 2.589f, 2.690f};
        float[] y = new float[16];
        System.arraycopy(x, 0, y, 0, 16);
        for (int j = 0; j < 10000000; j++) {
            for (int i = 0; i < 16; i++) {
                y[i] *= (x[i] / z[i]);
                y[i] = y[i] + 0.00001f;
            }
        }

        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);

        t1 = System.currentTimeMillis();
        x = new float[]{1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2.0f, 2.1f, 2.2f, 2.3f, 2.4f, 2.5f, 2.6f};
        z = new float[]{1.123f, 1.234f, 1.345f, 156.467f, 1.578f, 1.689f, 1.790f, 1.812f, 1.923f, 2.034f, 2.145f, 2.256f, 2.367f, 2.478f, 2.589f, 2.690f};
        y = new float[16];
        System.arraycopy(x, 0, y, 0, 16);
        for (int j = 0; j < 10000000; j++) {
            for (int i = 0; i < 16; i++) {
                y[i] *= (x[i] / z[i]);
            }
        }

        t2 = System.currentTimeMillis();
        System.out.println(t2-t1);

    }

}
