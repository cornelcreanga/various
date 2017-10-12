package compression.huffman;

//import net.jpountz.lz4.LZ4Compressor;
//import net.jpountz.lz4.LZ4Factory;
//import net.jpountz.lz4.LZ4FastDecompressor;
//import net.jpountz.lz4.LZ4SafeDecompressor;

import java.io.UnsupportedEncodingException;

public class LZ4Test {

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        LZ4Factory factory = LZ4Factory.fastestInstance();
//
//        byte[] data = "shoco is a C library to compress and decompress short strings. It is very fast and easy to use. The default compression model is optimized for english words, but you can generate your own compression model based on your specific input data.".getBytes("UTF-8");
//        data  = "how are you".getBytes("UTF-8");
//        final int decompressedLength = data.length;
//
//// compress data
//        LZ4Compressor compressor = factory.fastCompressor();
//        int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
//        byte[] compressed = new byte[maxCompressedLength];
//        int compressedLength = compressor.compress(data, 0, decompressedLength, compressed, 0, maxCompressedLength);
//        System.out.println(data.length);
//        System.out.println(compressedLength);
//
//
//// decompress data
//// - method 1: when the decompressed length is known
//        LZ4FastDecompressor decompressor = factory.fastDecompressor();
//        byte[] restored = new byte[decompressedLength];
//        int compressedLength2 = decompressor.decompress(compressed, 0, restored, 0, decompressedLength);
//// compressedLength == compressedLength2
//
//// - method 2: when the compressed length is known (a little slower)
//// the destination buffer needs to be over-sized
//        LZ4SafeDecompressor decompressor2 = factory.safeDecompressor();
//        int decompressedLength2 = decompressor2.decompress(compressed, 0, compressedLength, restored, 0);
//// decompressedLength == decompressedLength2
//    }

}
