package benchmark;

import java.io.FileOutputStream;
import java.io.IOException;

public class NaiveIODisk {

    public static void main(String[] args) throws IOException {
        long t1 = System.currentTimeMillis();
        int i = 0;
        byte[] b = new byte[1024*1024];
        while(i++<1024){
            for (int j = 0; j < b.length; j++) {
                b[j] = (byte)(Math.random()*Byte.MAX_VALUE);
            }
            try(FileOutputStream out =  new FileOutputStream("/tmp/test.tmp")){
                out.write(b);
            } catch (Exception e) {
                throw e;
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("1gb,time="+(t2-t1));

    }

}
