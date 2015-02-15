import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compress {



    public static byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
//        deflater.setStrategy(Deflater.HUFFMAN_ONLY);
        deflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        System.out.println("Original: " + data.length);
        System.out.println("Compressed: " + output.length);
        return output;
    }

    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        System.out.println("Original: " + data.length);
        System.out.println("Compressed: " + output.length);
        return output;
    }

    public static void main(String[] args) throws Exception {



        String inputString = "<message to='romeo@example.net' from='juliet@example.com/balcony' type='chat' xml:lang='en'><body>Wherefore art thou, Romeo?</body></message>";


//        String inputString = "The dictionary should consist of strings (byte sequences) that" + " are likely to be encountered later in the data to be compressed," + " with the most commonly used strings preferably put towards the " + "end of the dictionary. Using a dictionary is most useful when the" + " data to be compressed is short and can be predicted with good" + " accuracy; the data can then be compressed better than with the " + "default empty dictionary.";


        byte[] compressed = Compress.compress(inputString.getBytes("UTF-8"));
//        Compress.decompress(compressed);

//        System.out.println("uncompressedDataLength:"+inputString.length());
//
//        byte[] input = inputString.getBytes("UTF-8");
//        byte[] dict = "rdsusedusefulwhencanismostofstringscompresseddatatowithdictionarybethe".getBytes("UTF-8");
//
//        // Compress the bytes
//        byte[] output = new byte[1024];
//        Deflater compresser = new Deflater();
//        compresser.setInput(input);
//        compresser.setStrategy(Deflater.HUFFMAN_ONLY);
////        compresser.setDictionary(dict);
//        compresser.finish();
//        int compressedDataLength = compresser.deflate(output);
//
//        System.out.println("compressedDataLength:"+compressedDataLength);
//
//
//        // Decompress the bytes
//        Inflater decompresser = new Inflater();
//        decompresser.setInput(output, 0, compressedDataLength);
//        byte[] result = new byte[1024];
//
//        decompresser.inflate(result);
////        decompresser.setDictionary(dict);
//        int resultLength = decompresser.inflate(result);
//        decompresser.end();
//
//        // Decode the bytes into a String
//        String outputString = new String(result, 0, resultLength, "UTF-8");
//        System.out.println("Decompressed String: " + outputString);
    }
}
