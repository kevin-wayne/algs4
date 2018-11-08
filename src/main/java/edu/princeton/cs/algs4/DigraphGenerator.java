/******************************************************************************
 *  Compilation:  javac DigraphGenerator.java
 *  Execution:    java DigraphGenerator V E
 *  Dependencies: Digraph.java
 *
 *  A digraph generator.
 *  
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code DigraphGenerator} class provides static methods for creating
 *  various digraphs, including Erdos-Renyi random digraphs, random DAGs,
 *  random rooted trees, random rooted DAGs, random tournaments, path digraphs,
 *  cycle digraphs, and the complete digraph.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class DigraphGenerator {
    private static final class Edge implements Comparable<Edge> {
        private final int v;
        private final int w;

        private Edge(int v, int w) {
            this.v = v;
            this.w = w;
        }

        public int compareTo(Edge that) {
            if (this.v < that.v) return -1;
            if (this.v > that.v) return +1;
            if (this.w < that.w) return -1;
            if (this.w > that.w) return +1;
            return 0;
        }
    }

    // this class cannot be instantiated
    private DigraphGenerator() { }

    /**
     * Returns a random simple digraph containing {@code V} vertices and {@code E} edges.
     * @param V the number of vertices
     * @param E the number of vertices
     * @return a random simple digraph on {@code V} vertices, containing a total
     *     of {@code E} edges
     * @throws IllegalArgumentException if no such simple digraph exists
     */
    public static Digraph simple(int V, int E) {
        if (E > (long) V*(V-1)) throw new IllegalArgumentException("Too many edges");
        if (E < 0)              throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();
        while (G.E() < E) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            Edge e = new Edge(v, w);
            if ((v != w) && !set.contains(e)) {
                set.add(e);
                G.addEdge(v, w);
            }
        }
        return G;
    }

   /**
     * Returns a random simple digraph on {@code V} vertices, with an 
     * edge between any two vertices with probability {@code p}. This is sometimes
     * referred to as the Erdos-Renyi random digraph model.
     * This implementations takes time propotional to V^2 (even if {@code p} is small).
     * @param V the number of vertices
     * @param p the probability of choosing an edge
     * @return a random simple digraph on {@code V} vertices, with an edge between
     *     any two vertices with probability {@code p}
     * @throws IllegalArgumentException if probability is not between 0 and 1
     */
    public static Digraph simple(int V, double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        Digraph G = new Digraph(V);
        for (int v = 0; v < V; v++)
            for (int w = 0; w < V; w++)
                if (v != w)
                    if (StdRandom.bernoulli(p))
                        G.addEdge(v, w);
        return G;
    }

    /**
     * Returns the complete digraph on {@code V} vertices.
     * In a complete digraph, every pair of distinct vertices is connected
     * by two antiparallel edges. There are {@code V*(V-1)} edges.
     * @param V the number of vertices
     * @return the complete digraph on {@code V} vertices
     */
    public static Digraph complete(int V) {
        Digraph G = new Digraph(V);
        for (int v = 0; v < V; v++)
            for (int w = 0; w < V; w++)
                    if (v != w) G.addEdge(v, w);
        return G;
    }

    /**
     * Returns a random simple DAG containing {@code V} vertices and {@code E} edges.
     * Note: it is not uniformly selected at random among all such DAGs.
     * @param V the number of vertices
     * @param E the number of vertices
     * @return a random simple DAG on {@code V} vertices, containing a total
     *     of {@code E} edges
     * @throws IllegalArgumentException if no such simple DAG exists
     */
    public static Digraph dag(int V, int E) {
        if (E > (long) V*(V-1) / 2) throw new IllegalArgumentException("Too many edges");
        if (E < 0)                  throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        while (G.E() < E) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            Edge e = new Edge(v, w);
            if ((v < w) && !set.contains(e)) {
                set.add(e);
                G.addEdge(vertices[v], vertices[w]);
            }
        }
        return G;
    }

    // tournament
    /**
     * Returns a random tournament digraph on {@code V} vertices. A tournament digraph
     * is a DAG in which for every two vertices, there is one directed edge.
     * A tournament is an oriented complete graph.
     * @param V the number of vertices
     * @return a random tournament digraph on {@code V} vertices
     */
    public static Digraph tournament(int V) {
        Digraph G = new Digraph(V);
        for (int v = 0; v < G.V(); v++) {
            for (int w = v+1; w < G.V(); w++) {
                if (StdRandom.bernoulli(0.5)) G.addEdge(v, w);
                else                          G.addEdge(w, v);
            }
        }
        return G;
    }

    /**
     * Returns a complete rooted-in DAG on {@code V} vertices.
     * A rooted in-tree is a DAG in which there is a single vertex
     * reachable from every other vertex. A complete rooted in-DAG
     * has V*(V-1)/2 edges.
     * @param V the number of vertices
     * @return a complete rooted-in DAG on {@code V} vertices
     */
    public static Digraph completeRootedInDAG(int V) {
        Digraph G = new Digraph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < V; i++)
            for (int j = i+1; j < V; j++)
                 G.addEdge(vertices[i], vertices[j]);

        return G;
    }

    /**
     * Returns a random rooted-in DAG on {@code V} vertices and {@code E} edges.
     * A rooted in-tree is a DAG in which there is a single vertex
     * reachable from every other vertex.
     * The DAG returned is not chosen uniformly at random among all such DAGs.
     * @param V the number of vertices
     * @param E the number of edges
     * @return a random rooted-in DAG on {@code V} vertices and {@code E} edges
     */
    public static Digraph rootedInDAG(int V, int E) {
        if (E > (long) V*(V-1) / 2) throw new IllegalArgumentException("Too many edges");
        if (E < V-1)                throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();

        // fix a topological order
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);

        // one edge pointing from each vertex, other than the root = vertices[V-1]
        for (int v = 0; v < V-1; v++) {
            int w = StdRandom.uniform(v+1, V);
            Edge e = new Edge(v, w);
            set.add(e);
            G.addEdge(vertices[v], vertices[w]);
        }

        while (G.E() < E) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            Edge e = new Edge(v, w);
            if ((v < w) && !set.contains(e)) {
                set.add(e);
                G.addEdge(vertices[v], vertices[w]);
            }
        }
        return G;
    }

    /**
     * Returns a complete rooted-out DAG on {@code V} vertices.
     * A rooted out-tree is a DAG in which every vertex is reachable
     * from a single vertex. A complete rooted in-DAG has V*(V-1)/2 edges.
     * @param V the number of vertices
     * @return a complete rooted-out DAG on {@code V} vertices
     */
    public static Digraph completeRootedOutDAG(int V) {
        Digraph G = new Digraph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < V; i++)
            for (int j = i+1; j < V; j++)
                 G.addEdge(vertices[j], vertices[i]);

        return G;
    }

    /**
     * Returns a random rooted-out DAG on {@code V} vertices and {@code E} edges.
     * A rooted out-tree is a DAG in which every vertex is reachable from a
     * single vertex.
     * The DAG returned is not chosen uniformly at random among all such DAGs.
     * @param V the number of vertices
     * @param E the number of edges
     * @return a random rooted-out DAG on {@code V} vertices and {@code E} edges
     */
    public static Digraph rootedOutDAG(int V, int E) {
        if (E > (long) V*(V-1) / 2) throw new IllegalArgumentException("Too many edges");
        if (E < V-1)                throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();

        // fix a topological order
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);

        // one edge pointing from each vertex, other than the root = vertices[V-1]
        for (int v = 0; v < V-1; v++) {
            int w = StdRandom.uniform(v+1, V);
            Edge e = new Edge(w, v);
            set.add(e);
            G.addEdge(vertices[w], vertices[v]);
        }

        while (G.E() < E) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            Edge e = new Edge(w, v);
            if ((v < w) && !set.contains(e)) {
                set.add(e);
                G.addEdge(vertices[w], vertices[v]);
            }
        }
        return G;
    }

    /**
     * Returns a random rooted-in tree on {@code V} vertices.
     * A rooted in-tree is an oriented tree in which there is a single vertex
     * reachable from every other vertex.
     * The tree returned is not chosen uniformly at random among all such trees.
     * @param V the number of vertices
     * @return a random rooted-in tree on {@code V} vertices
     */
    public static Digraph rootedInTree(int V) {
        return rootedInDAG(V, V-1);
    }

    /**
     * Returns a random rooted-out tree on {@code V} vertices. A rooted out-tree
     * is an oriented tree in which each vertex is reachable from a single vertex.
     * It is also known as a <em>arborescence</em> or <em>branching</em>.
     * The tree returned is not chosen uniformly at random among all such trees.
     * @param V the number of vertices
     * @return a random rooted-out tree on {@code V} vertices
     */
    public static Digraph rootedOutTree(int V) {
        return rootedOutDAG(V, V-1);
    }

    /**
     * Returns a path digraph on {@code V} vertices.
     * @param V the number of vertices in the path
     * @return a digraph that is a directed path on {@code V} vertices
     */
    public static Digraph path(int V) {
        Digraph G = new Digraph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < V-1; i++) {
            G.addEdge(vertices[i], vertices[i+1]);
        }
        return G;
    }

    /**
     * Returns a complete binary tree digraph on {@code V} vertices.
     * @param V the number of vertices in the binary tree
     * @return a digraph that is a complete binary tree on {@code V} vertices
     */
    public static Digraph binaryTree(int V) {
        Digraph G = new Digraph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 1; i < V; i++) {
            G.addEdge(vertices[i], vertices[(i-1)/2]);
        }
        return G;
    }

    /**
     * Returns a cycle digraph on {@code V} vertices.
     * @param V the number of vertices in the cycle
     * @return a digraph that is a directed cycle on {@code V} vertices
     */
    public static Digraph cycle(int V) {
        Digraph G = new Digraph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < V-1; i++) {
            G.addEdge(vertices[i], vertices[i+1]);
        }
        G.addEdge(vertices[V-1], vertices[0]);
        return G;
    }

    /**
     * Returns an Eulerian cycle digraph on {@code V} vertices.
     *
     * @param  V the number of vertices in the cycle
     * @param  E the number of edges in the cycle
     * @return a digraph that is a directed Eulerian cycle on {@code V} vertices
     *         and {@code E} edges
     * @throws IllegalArgumentException if either {@code V <= 0} or {@code E <= 0}
     */
    public static Digraph eulerianCycle(int V, int E) {
        if (E <= 0)
            throw new IllegalArgumentException("An Eulerian cycle must have at least one edge");
        if (V <= 0)
            throw new IllegalArgumentException("An Eulerian cycle must have at least one vertex");
        Digraph G = new Digraph(V);
        int[] vertices = new int[E];
        for (int i = 0; i < E; i++)
            vertices[i] = StdRandom.uniform(V);
        for (int i = 0; i < E-1; i++) {
            G.addEdge(vertices[i], vertices[i+1]);
        }
        G.addEdge(vertices[E-1], vertices[0]);
        return G;
    }

    /**
     * Returns an Eulerian path digraph on {@code V} vertices.
     *
     * @param  V the number of vertices in the path
     * @param  E the number of edges in the path
     * @return a digraph that is a directed Eulerian path on {@code V} vertices
     *         and {@code E} edges
     * @throws IllegalArgumentException if either {@code V <= 0} or {@code E < 0}
     */
    public static Digraph eulerianPath(int V, int E) {
        if (E < 0)
            throw new IllegalArgumentException("negative number of edges");
        if (V <= 0)
            throw new IllegalArgumentException("An Eulerian path must have at least one vertex");
        Digraph G = new Digraph(V);
        int[] vertices = new int[E+1];
        for (int i = 0; i < E+1; i++)
            vertices[i] = StdRandom.uniform(V);
        for (int i = 0; i < E; i++) {
            G.addEdge(vertices[i], vertices[i+1]);
        }
        return G;
    }

   /**
     * Returns a random simple digraph on {@code V} vertices, {@code E}
     * edges and (at least) {@code c} strong components. The vertices are randomly
     * assigned integer labels between {@code 0} and {@code c-1} (corresponding to 
     * strong components). Then, a strong component is creates among the vertices
     * with the same label. Next, random edges (either between two vertices with
     * the same labels or from a vetex with a smaller label to a vertex with a 
     * larger label). The number of components will be equal to the number of
     * distinct labels that are assigned to vertices.
     *
     * @param V the number of vertices
     * @param E the number of edges
     * @param c the (maximum) number of strong components
     * @return a random simple digraph on {@code V} vertices and
               {@code E} edges, with (at most) {@code c} strong components
     * @throws IllegalArgumentException if {@code c} is larger than {@code V}
     */
    public static Digraph strong(int V, int E, int c) {
        if (c >= V || c <= 0)
            throw new IllegalArgumentException("Number of components must be between 1 and V");
        if (E <= 2*(V-c))
            throw new IllegalArgumentException("Number of edges must be at least 2(V-c)");
        if (E > (long) V*(V-1) / 2)
            throw new IllegalArgumentException("Too many edges");

        // the digraph
        Digraph G = new Digraph(V);

        // edges added to G (to avoid duplicate edges)
        SET<Edge> set = new SET<Edge>();

        int[] label = new int[V];
        for (int v = 0; v < V; v++)
            label[v] = StdRandom.uniform(c);

        // make all vertices with label c a strong component by
        // combining a rooted in-tree and a rooted out-tree
        for (int i = 0; i < c; i++) {
            // how many vertices in component c
            int count = 0;
            for (int v = 0; v < G.V(); v++) {
                if (label[v] == i) count++;
            }

            // if (count == 0) System.err.println("less than desired number of strong components");

            int[] vertices = new int[count];
            int j = 0;
            for (int v = 0; v < V; v++) {
                if (label[v] == i) vertices[j++] = v;
            }
            StdRandom.shuffle(vertices);

            // rooted-in tree with root = vertices[count-1]
            for (int v = 0; v < count-1; v++) {
                int w = StdRandom.uniform(v+1, count);
                Edge e = new Edge(w, v);
                set.add(e);
                G.addEdge(vertices[w], vertices[v]);
            }

            // rooted-out tree with root = vertices[count-1]
            for (int v = 0; v < count-1; v++) {
                int w = StdRandom.uniform(v+1, count);
                Edge e = new Edge(v, w);
                set.add(e);
                G.addEdge(vertices[v], vertices[w]);
            }
        }

        while (G.E() < E) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            Edge e = new Edge(v, w);
            if (!set.contains(e) && v != w && label[v] <= label[w]) {
                set.add(e);
                G.addEdge(v, w);
            }
        }

        return G;
    }

    /**
     * Unit tests the {@code DigraphGenerator} library.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        StdOut.println("complete graph");
        StdOut.println(complete(V));
        StdOut.println();

        StdOut.println("simple");
        StdOut.println(simple(V, E));
        StdOut.println();

        StdOut.println("path");
        StdOut.println(path(V));
        StdOut.println();

        StdOut.println("cycle");
        StdOut.println(cycle(V));
        StdOut.println();

        StdOut.println("Eulierian path");
        StdOut.println(eulerianPath(V, E));
        StdOut.println();

        StdOut.println("Eulierian cycle");
        StdOut.println(eulerianCycle(V, E));
        StdOut.println();

        StdOut.println("binary tree");
        StdOut.println(binaryTree(V));
        StdOut.println();

        StdOut.println("tournament");
        StdOut.println(tournament(V));
        StdOut.println();

        StdOut.println("DAG");
        StdOut.println(dag(V, E));
        StdOut.println();

        StdOut.println("rooted-in DAG");
        StdOut.println(rootedInDAG(V, E));
        StdOut.println();

        StdOut.println("rooted-out DAG");
        StdOut.println(rootedOutDAG(V, E));
        StdOut.println();

        StdOut.println("rooted-in tree");
        StdOut.println(rootedInTree(V));
        StdOut.println();

        StdOut.println("rooted-out DAG");
        StdOut.println(rootedOutTree(V));
        StdOut.println();
    }

}

/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
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
