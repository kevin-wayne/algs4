/******************************************************************************
 *  Compilation:  javac PatriciaST.java
 *  Execution:    java PatriciaST
 *  Dependencies: StdOut.java StdRandom.java Queue.java
 *  Data files:   n/a
 *
 *  A symbol table implementation based on PATRICIA.
 *
 *  % java PatriciaST 1000000 1
 *  Creating dataset (1000000 items)...
 *  Shuffling...
 *  Adding (1000000 items)...
 *  Iterating...
 *  1000000 items iterated
 *  Shuffling...
 *  Deleting (500000 items)...
 *  Iterating...
 *  500000 items iterated
 *  Checking...
 *  500000 items found and 500000 (deleted) items missing
 *  Deleting the rest (500000 items)...
 *  PASS 1 TESTS SUCCEEDED
 *  %
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code PatriciaST} class provides an implementation of an unordered
 *  symbol table of key-value pairs, with the restriction that the key is of
 *  class {@link java.lang.String}. It supports the usual <em>put</em>,
 *  <em>get</em>, <em>contains</em>, <em>delete</em>, <em>size</em>, and
 *  <em>is-empty</em> methods. It also provides a <em>keys</em> method for
 *  iterating over all of the keys. A symbol table implements the
 *  <em>associative array</em> abstraction: when associating a value with a key
 *  that is already in the symbol table, the convention is to replace the old
 *  value with the new value. Unlike {@link java.util.Map}, this class uses the
 *  convention that values cannot be {@code null}—setting the value
 *  associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This unordered symbol table class implements PATRICIA (Practical Algorithm
 *  to Retrieve Information Coded In Alphanumeric). In spite of the acronym,
 *  string keys are not limited to alphanumeric content. A key may possess any
 *  string value, except for the string of zero length (the empty string).
 *  <p>
 *  Unlike other generic symbol table implementations that can accept a
 *  parameterized key type, this symbol table class can only accommodate keys
 *  of class {@link java.lang.String}. This unfortunate restriction stems from a
 *  limitation in Java. Although Java provides excellent support for generic
 *  programming, the current infrastructure somewhat limits generic collection
 *  implementations to those that employ comparison-based or hash-based methods.
 *  PATRICIA does not employ comparisons or hashing; instead, it relies on
 *  bit-test operations. Because Java does not furnish any generic abstractions
 *  (or implementations) for bit-testing the contents of an object, providing
 *  support for generic keys using PATRICIA does not seem practical.
 *  <p>
 *  PATRICIA is a variation of a trie, and it is often classified as a
 *  space-optimized trie. In a classical trie, each level represents a
 *  subsequent digit in a key. In PATRICIA, nodes only exist to identify the
 *  digits (bits) that distinguish the individual keys within the trie. Because
 *  PATRICIA uses a radix of two, each node has only two children, like a binary
 *  tree. Also like a binary tree, the number of nodes, within the trie, equals
 *  the number of keys. Consequently, some classify PATRICIA as a tree.
 *  <p>
 *  The analysis of PATRICIA is complicated. The theoretical wost-case
 *  performance for a <em>get</em>, <em>put</em>, or <em>delete</em> operation
 *  is <strong>O(N)</strong>, when <strong>N</strong> is less than
 *  <strong>W</strong> (where <strong>W</strong> is the length in bits of the
 *  longest key), and <strong>O(W)</strong>, when <strong>N</strong> is greater
 *  than <strong>W</strong>. However, the worst case is unlikely to occur with
 *  typical use. The average (and usual) performance of PATRICIA is
 *  approximately <strong>~lg N</strong> for each <em>get</em>, <em>put</em>, or
 *  <em>delete</em> operation. Although this appears to put PATRICIA on the same
 *  footing as binary trees, this time complexity represents the number of
 *  single-bit test operations (under PATRICIA), and not full-key comparisons
 *  (as required by binary trees). After the single-bit tests conclude, PATRICIA
 *  requires just one full-key comparison to confirm the existence (or absence)
 *  of the key (per <em>get</em>, <em>put</em>, or <em>delete</em> operation).
 *  <p>
 *  In practice, decent implementations of PATRICIA can often outperform
 *  balanced binary trees, and even hash tables. Although this particular
 *  implementation performs well, the source code was written with an emphasis
 *  on clarity, and not performance. PATRICIA performs admirably when its
 *  bit-testing loops are well tuned. Consider using the source code as a guide,
 *  should you need to produce an optimized implementation, for anther key type,
 *  or in another programming language.
 *  <p>
 *  Other resources for PATRICIA:<br>
 *  Sedgewick, R. (1990) <i>Algorithms in C</i>, Addison-Wesley<br>
 *  Knuth, D. (1973) <i>The Art of Computer Programming</i>, Addison-Wesley<br>
 *
 *  @author John Hentosh (based on an implementation by Robert Sedgewick)
 */
public class PatriciaST<Value> {
    private Node head;
    private int count;

    /* An inner Node class specifies the objects that hold each key-value pair.
     * The b value indicates the relevant bit position.
     */
    private class Node {
        private Node left, right;
        private String key;
        private Value val;
        private int b;

        public Node(String key, Value val, int b) {
            this.key = key;
            this.val = val;
            this.b = b;
        }
    };

    /**
     * Initializes an empty PATRICIA-based symbol table.
     */
    /* The constructor creates a head (sentinel) node that contains a
     * zero-length string.
     */
    public PatriciaST() {
        head = new Node("", null, 0);
        head.left = head;
        head.right = head;
        count = 0;
    }

    /**
     * Places a key-value pair into the symbol table. If the table already
     * contains the specified key, then its associated value becomes updated.
     * If the value provided is {@code null}, then the key becomes removed
     * from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     * @throws IllegalArgumentException if {@code key} is the empty string.
     */
    public void put(String key, Value val) {
        if (key == null) throw new IllegalArgumentException("called put(null)");
        if (key.length() == 0) throw new IllegalArgumentException("invalid key");
        if (val == null) delete(key);
        Node p;
        Node x = head;
        do {
            p = x;
            if (safeBitTest(key, x.b)) x = x.right;
            else                       x = x.left;
        } while (p.b < x.b);
        if (!x.key.equals(key)) {
            int b = firstDifferingBit(x.key, key);
            x = head;
            do {
                p = x;
                if (safeBitTest(key, x.b)) x = x.right;
                else                       x = x.left;
            } while (p.b < x.b && x.b < b);
            Node t = new Node(key, val, b);
            if (safeBitTest(key, b)) {
                t.left  = x;
                t.right = t;
            }
            else {
                t.left  = t;
                t.right = x;
            }
            if (safeBitTest(key, p.b)) p.right = t;
            else                       p.left  = t;
            count++;
        }
        else x.val = val;
    }

    /**
     * Retrieves the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the
     * symbol table and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     * @throws IllegalArgumentException if {@code key} is the empty string.
     */
    public Value get(String key) {
        if (key == null) throw new IllegalArgumentException("called get(null)");
        if (key.length() == 0) throw new IllegalArgumentException("invalid key");
        Node p;
        Node x = head;
        do {
            p = x;
            if (safeBitTest(key, x.b)) x = x.right;
            else                       x = x.left;
        } while (p.b < x.b);
        if (x.key.equals(key)) return x.val;
        else                   return null;
    }

    /**
     * Removes a key and its associated value from the symbol table, if it
     * exists.
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     * @throws IllegalArgumentException if {@code key} is the empty string.
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("called delete(null)");
        if (key.length() == 0) throw new IllegalArgumentException("invalid key");
        Node g;             // previous previous (grandparent)
        Node p = head;      // previous (parent)
        Node x = head;      // node to delete
        do {
            g = p;
            p = x;
            if (safeBitTest(key, x.b)) x = x.right;
            else                       x = x.left;
        } while (p.b < x.b);
        if (x.key.equals(key)) {
            Node z;
            Node y = head;
            do {            // find the true parent (z) of x
                z = y;
                if (safeBitTest(key, y.b)) y = y.right;
                else                       y = y.left;
            } while (y != x);
            if (x == p) {   // case 1: remove (leaf node) x
                Node c;     // child of x
                if (safeBitTest(key, x.b)) c = x.left;
                else                       c = x.right;
                if (safeBitTest(key, z.b)) z.right = c;
                else                       z.left  = c;
            }
            else {          // case 2: p replaces (internal node) x
                Node c;     // child of p
                if (safeBitTest(key, p.b)) c = p.left;
                else                       c = p.right;
                if (safeBitTest(key, g.b)) g.right = c;
                else                       g.left  = c;
                if (safeBitTest(key, z.b)) z.right = p;
                else                       z.left  = p;
                p.left = x.left;
                p.right = x.right;
                p.b = x.b;
            }
            count--;
        }
    }

    /**
     * Returns {@code true} if the key-value pair, specified by the given
     * key, exists within the symbol table.
     * @param key the key
     * @return {@code true} if this symbol table contains the given
     * {@code key} and {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     * @throws IllegalArgumentException if {@code key} is the empty string.
     */
    public boolean contains(String key) {
        return get(key) != null;
    }

    /**
     * Returns {@code true} if the symbol table is empty.
     * @return {@code true} if this symbol table is empty and
     * {@code false} otherwise
     */
    boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of key-value pairs within the symbol table.
     * @return the number of key-value pairs within this symbol table
     */
    int size() {
        return count;
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named
     * {@code st}, use the foreach notation:
     * {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        if (head.left  != head) keys(head.left,  0, queue);
        if (head.right != head) keys(head.right, 0, queue);
        return queue;
    }

    private void keys(Node x, int b, Queue<String> queue) {
        if (x.b > b) {
            keys(x.left, x.b, queue);
            queue.enqueue(x.key);
            keys(x.right, x.b, queue);
        }
    }

    /* The safeBitTest function logically appends a terminating sequence (when
     * required) to extend (logically) the string beyond its length.
     *
     * The inner loops of the get and put methods flow much better when they
     * are not concerned with the lengths of strings, so a trick is employed to
     * allow the get and put methods to view every string as an "infinite"
     * sequence of bits. Logically, every string gets a '\uffff' character,
     * followed by an "infinite" sequence of '\u0000' characters, appended to
     * the end.
     *
     * Note that the '\uffff' character serves to mark the end of the string,
     * and it is necessary. Simply padding with '\u0000' is insufficient to
     * make all unique Unicode strings "look" unique to the get and put methods
     * (because these methods do not regard string lengths).
     */
    private static boolean safeBitTest(String key, int b) {
        if (b < key.length() * 16)      return bitTest(key, b) != 0;
        if (b > key.length() * 16 + 15) return false;   // padding
        /* 16 bits of 0xffff */         return true;    // end marker
    }

    private static int bitTest(String key, int b) {
        return (key.charAt(b >>> 4) >>> (b & 0xf)) & 1;
    }

    /* Like the safeBitTest function, the safeCharAt function makes every
     * string look like an "infinite" sequence of characters. Logically, every
     * string gets a '\uffff' character, followed by an "infinite" sequence of
     * '\u0000' characters, appended to the end.
     */
    private static int safeCharAt(String key, int i) {
        if (i < key.length()) return key.charAt(i);
        if (i > key.length()) return 0x0000;            // padding
        else                  return 0xffff;            // end marker
    }

    /* For efficiency's sake, the firstDifferingBit function compares entire
     * characters first, and then considers the individual bits (once it finds
     * two characters that do not match). Also, the least significant bits of
     * an individual character are examined first. There are many Unicode
     * alphabets where most (if not all) of the "action" occurs in the least
     * significant bits.
     *
     * Notice that the very first character comparison excludes the
     * least-significant bit. The firstDifferingBit function must never return
     * zero; otherwise, a node would become created as a child to the head
     * (sentinel) node that matches the bit-index value (zero) stored in the
     * head node. This would violate the invariant that bit-index values
     * increase as you descend into the trie.
     */
    private static int firstDifferingBit(String k1, String k2) {
        int i = 0;
        int c1 = safeCharAt(k1, 0) & ~1;
        int c2 = safeCharAt(k2, 0) & ~1;
        if (c1 == c2) {
            i = 1;
            while (safeCharAt(k1, i) == safeCharAt(k2, i)) i++;
            c1 = safeCharAt(k1, i);
            c2 = safeCharAt(k2, i);
        }
        int b = 0;
        while (((c1 >>> b) & 1) == ((c2 >>> b) & 1)) b++;
        return i * 16 + b;
    }

    /**
     * Unit tests the {@code PatriciaST} data type.
     * This test fixture runs a series of tests on a randomly generated dataset.
     * You may specify up to two integer parameters on the command line. The
     * first parameter indicates the size of the dataset. The second parameter
     * controls the number of passes (a new random dataset becomes generated at
     * the start of each pass).
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        PatriciaST<Integer> st = new PatriciaST<Integer>();
        int limitItem = 1000000;
        int limitPass = 1;
        int countPass = 0;
        boolean ok = true;

        if (args.length > 0) limitItem = Integer.parseInt(args[0]);
        if (args.length > 1) limitPass = Integer.parseInt(args[1]);

        do {
            String[] a = new String[limitItem];
            int[]    v = new int[limitItem];

            StdOut.printf("Creating dataset (%d items)...\n", limitItem);
            for (int i = 0; i < limitItem; i++) {
                a[i] = Integer.toString(i, 16);
                v[i] = i;
            }

            StdOut.printf("Shuffling...\n");
            StdRandom.shuffle(v);

            StdOut.printf("Adding (%d items)...\n", limitItem);
            for (int i = 0; i < limitItem; i++)
                st.put(a[v[i]], v[i]);

            int countKeys = 0;
            StdOut.printf("Iterating...\n");
            for (String key : st.keys()) countKeys++;
            StdOut.printf("%d items iterated\n", countKeys);
            if (countKeys != limitItem) ok = false;
            if (countKeys != st.size()) ok = false;

            StdOut.printf("Shuffling...\n");
            StdRandom.shuffle(v);

            int limitDelete = limitItem / 2;
            StdOut.printf("Deleting (%d items)...\n", limitDelete);
            for (int i = 0; i < limitDelete; i++)
                st.delete(a[v[i]]);

            countKeys = 0;
            StdOut.printf("Iterating...\n");
            for (String key : st.keys()) countKeys++;
            StdOut.printf("%d items iterated\n", countKeys);
            if (countKeys != limitItem - limitDelete) ok = false;
            if (countKeys != st.size())               ok = false;

            int countDelete = 0;
            int countRemain = 0;
            StdOut.printf("Checking...\n");
            for (int i = 0; i < limitItem; i++) {
                if (i < limitDelete) {
                    if (!st.contains(a[v[i]])) countDelete++;
                }
                else {
                    int val = st.get(a[v[i]]);
                    if (val == v[i]) countRemain++;
                }
            }
            StdOut.printf("%d items found and %d (deleted) items missing\n",
                countRemain, countDelete);
            if (countRemain + countDelete != limitItem) ok = false;
            if (countRemain               != st.size()) ok = false;
            if (st.isEmpty())                           ok = false;

            StdOut.printf("Deleting the rest (%d items)...\n",
                limitItem - countDelete);
            for (int i = countDelete; i < limitItem; i++)
                st.delete(a[v[i]]);
            if (!st.isEmpty()) ok = false;

            countPass++;
            if (ok) StdOut.printf("PASS %d TESTS SUCCEEDED\n", countPass);
            else    StdOut.printf("PASS %d TESTS FAILED\n",    countPass);
        } while (ok && countPass < limitPass);

        if (!ok) throw new java.lang.RuntimeException("TESTS FAILED");
    }
}

/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
