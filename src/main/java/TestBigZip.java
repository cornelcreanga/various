import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestBigZip {

    public static void main(String[] args) throws IOException {

        // out put file
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream("/home/ccreanga/test.zip"));
        for (int i = 0; i < 1 ; i++) {
            byte[] b = new byte[1024*1024];
            for (int j = 0; j < b.length; j++) {
                b[j] = 65;
            }
            out.putNextEntry(new ZipEntry("zipped"+i));
            for (int j = 0; j < 1000; j++) {
                out.write(b,0,b.length);
            }
            out.closeEntry();
        }
        // name the file inside the zip  file
        out.close();


    }
}
