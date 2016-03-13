/*************************************************************************
 *  Compilation:  javac MinPQ.java
 *  Execution:    java MinPQ < input.txt
 *
 *  Generic min priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order.
 *
 *  % java MinPQ < tinyPQ.txt
 *  E A E (6 left on pq)
 *
 *  We use a one-based array to simplify parent and child calculations.
 *
 *  Can be optimized by replacing full exchanges with half exchanges
 *  (ala insertion sort).
 *
 *************************************************************************/

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DedicatedMinPQ<Key> {
//    private Huffman.Node[] pq;                    // store items at indices 1 to N
//    private int N;                       // number of items on priority queue
//
//    /**
//     * Initializes an empty priority queue with the given initial capacity.
//     * @param initCapacity the initial capacity of the priority queue
//     */
//    public DedicatedMinPQ(int initCapacity) {
//        pq = new Huffman.Node[initCapacity + 1];
//        N = 0;
//    }
//
//    /**
//     * Is the priority queue empty?
//     * @return true if the priority queue is empty; false otherwise
//     */
//    public boolean isEmpty() {
//        return N == 0;
//    }
//
//    /**
//     * Returns the number of keys on the priority queue.
//     * @return the number of keys on the priority queue
//     */
//    public int size() {
//        return N;
//    }
//
//    /**
//     * Returns a smallest key on the priority queue.
//     * @return a smallest key on the priority queue
//     * @throws java.util.NoSuchElementException if priority queue is empty
//     */
//    public Huffman.Node min() {
//        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
//        return pq[1];
//    }
//
//    // helper function to double the size of the heap array
//    private void resize(int capacity) {
//        assert capacity > N;
//        Huffman.Node[] temp = new Huffman.Node[capacity];
//        for (int i = 1; i <= N; i++) temp[i] = pq[i];
//        pq = temp;
//    }
//
//    /**
//     * Adds a new key to the priority queue.
//     * @param x the key to add to the priority queue
//     */
//    public void insert(Huffman.Node x) {
//        // double size of array if necessary
//        if (N == pq.length - 1) resize(2 * pq.length);
//
//        // add x, and percolate it up to maintain heap invariant
//        pq[++N] = x;
//        swim(N);
//    }
//
//    /**
//     * Removes and returns a smallest key on the priority queue.
//     * @return a smallest key on the priority queue
//     * @throws java.util.NoSuchElementException if the priority queue is empty
//     */
//    public Huffman.Node delMin() {
//        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
//        exch(1, N);
//        Huffman.Node min = pq[N--];
//        sink(1);
//        pq[N+1] = null;         // avoid loitering and help with garbage collection
//        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length  / 2);
//        assert isMinHeap();
//        return min;
//    }
//
//
//    /***********************************************************************
//     * Helper functions to restore the heap invariant.
//     **********************************************************************/
//
//    private void swim(int k) {
//        while (k > 1 && greater(k/2, k)) {
//            exch(k, k/2);
//            k = k/2;
//        }
//    }
//
//    private void sink(int k) {
//        while (2*k <= N) {
//            int j = 2*k;
//            if (j < N && greater(j, j+1)) j++;
//            if (!greater(k, j)) break;
//            exch(k, j);
//            k = j;
//        }
//    }
//
//    /***********************************************************************
//     * Helper functions for compares and swaps.
//     **********************************************************************/
//    private boolean greater(int i, int j) {
//        return pq[i].freq-pq[j].freq > 0;
//    }
//
//    private void exch(int i, int j) {
//        Huffman.Node swap = pq[i];
//        pq[i] = pq[j];
//        pq[j] = swap;
//    }
//
//    // is pq[1..N] a min heap?
//    private boolean isMinHeap() {
//        return isMinHeap(1);
//    }
//
//    // is subtree of pq[1..N] rooted at k a min heap?
//    private boolean isMinHeap(int k) {
//        if (k > N) return true;
//        int left = 2*k, right = 2*k + 1;
//        if (left  <= N && greater(k, left))  return false;
//        if (right <= N && greater(k, right)) return false;
//        return isMinHeap(left) && isMinHeap(right);
//    }
//
//
//    /***********************************************************************
//     * Iterators
//     **********************************************************************/

}