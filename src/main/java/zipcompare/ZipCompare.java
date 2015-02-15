package zipcompare;

import zipcompare.DifferenceCalculator;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipCompare {

    public static void compare(ZipFile x, ZipFile y) throws Exception{

        DifferenceCalculator diff = new DifferenceCalculator(x,y);
        diff.setIgnoreTimestamps(true);
        diff.setVerbose(true);
        diff.setIgnoreCVSFiles(true);
        System.out.println(diff.getDifferences().toString());

    }

    public static final void unzip(File zip, File extractTo)
            throws Exception {
        if (extractTo.exists() && extractTo.isDirectory() == false) {
            throw new Exception("Could not extract archive into "
                    + extractTo.getAbsolutePath()
                    + " because is not a directory");
        }

        if (extractTo.exists() == false) {
            extractTo.mkdir();
        }

        try {
            ZipFile archive = new ZipFile(zip);
            Enumeration<?> e = archive.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                File file = new File(extractTo, entry.getName());
                if (entry.isDirectory() && !file.exists()) {
                    file.mkdirs();
                } else {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    InputStream in = archive.getInputStream(entry);
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(file));

                    byte[] buffer = new byte[1000];
                    int read;

                    while (-1 != (read = in.read(buffer))) {
                        out.write(buffer, 0, read);
                    }

                    in.close();
                    out.close();
                }
            }
        } catch (IOException ex) {
            throw new Exception("Could not unzip archive file "
                    + zip.getPath(), ex);
        }
    }



    public static void main(String[]args) throws Exception {

        File f = new File("/Users/corneliucreanga/old_code/viewer_old.zip");
        File f2 = new File("/Users/corneliucreanga/old_code/viewer_new.zip");
        ZipFile zp1 = new ZipFile(f);
        ZipFile zp2 = new ZipFile(f2);
        //  unzip(f,new File("/Users/otudor/Desktop/old/unzipped"));
        System.out.println(f.canRead());
        compare(zp1, zp2);
    }


}
