package compression.huffman;

import java.util.BitSet;

public class Huffman {

    private Node huffmanRoot;
    // alphabet size of extended ASCII
    private static final int R = 256;

    // Huffman trie node
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            return (left == null && right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }


    // compress bytes from standard input and write to standard output
    public byte[] compress(String s) {
        // read the input
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        for (char anInput : input) freq[anInput]++;

        // build Huffman trie
        Node root = buildTrie(freq);
        huffmanRoot = root;

        // build code table
        String[] st = new String[R];
        buildCode(st, root, "");

//        System.out.println(s.length());
        int len = 0;


        BitSet bitSet = new BitSet(s.length()+s.length()/2);
        int counter = 0;
        //bitSet.
        for (char anInput : input) {
            String code = st[anInput];
            for (int j = 0; j < code.length(); j++){
                bitSet.set(counter++,code.charAt(j) == '1');
            }
        }


//        // use Huffman code to encode input
//        for (int i = 0; i < input.length; i++) {
//            String code = st[input[i]];
//            for (int j = 0; j < code.length(); j++) {
//                if (code.charAt(j) == '0') {
//                    BinaryStdOut.write(false);
//                }
//                else if (code.charAt(j) == '1') {
//                    BinaryStdOut.write(true);
//                }
//                else throw new IllegalStateException("Illegal state");
//            }
//        }
//
//        // close output stream
//        BinaryStdOut.close();
        return bitSet.toByteArray();
    }

    // build the Huffman trie given frequencies
    private Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // special case in case there is only one character with a nonzero frequency
        if (pq.size() == 1) {
            if (freq['\0'] == 0) pq.insert(new Node('\0', 0, null, null));
            else                 pq.insert(new Node('\1', 0, null, null));
        }

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left  = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }


    // write bitstring-encoded trie to standard output
    private void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }

    // make a lookup table from symbols and their encodings
    private void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
        }
    }


    // expand Huffman-encoded input from standard input and write to standard output
    public void expand(byte[] compressed) {



        BitSet bitSet = BitSet.valueOf(compressed);


        int counter = 0;
        // decode using the Huffman trie
        while(counter<bitSet.length()){
//        for (int i = 0; i < length; i++) {
            Node x = huffmanRoot;
            while (!x.isLeaf()) {
                boolean bit = bitSet.get(counter++);
                if (bit) x = x.right;
                else     x = x.left;
            }
//            BinaryStdOut.write(x.ch, 8);
        }
//        BinaryStdOut.close();
    }


    private Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        }
        else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }


    public static void main(String[] args) {
        String toCompress = "<message to='romeo@example.net' from='juliet@example.com/balcony' type='chat' xml:lang='en'><body>Wherefore art thou, Romeo?</body></message>";
        for (int i = 0; i < 100000; i++) {
            Huffman huffman = new Huffman();
            byte[] compressed = huffman.compress(toCompress);
            huffman.expand(compressed);
        }
        System.gc();System.gc();System.gc();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            Huffman huffman = new Huffman();
            huffman.compress(toCompress);
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);
//        for (int i = 0; i < 100000; i++) compress(toCompress);
//        byte[] compressed = compress(toCompress);
//        System.out.println(compressed.length);
//        BitSet bitSet = BitSet.valueOf(compressed);
//        expand(compressed);
    }

}
