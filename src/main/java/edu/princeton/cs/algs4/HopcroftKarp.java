/******************************************************************************
 *  Compilation:  javac HopcroftKarp.java
 *  Execution:    java HopcroftKarp V1 V2 E
 *  Dependencies: FordFulkerson.java FlowNetwork.java FlowEdge.java
 *                BipartiteX.java
 *
 *  Find a maximum cardinality matching (and minimum cardinality vertex cover)
 *  in a bipartite graph using Hopcroft-Karp algorithm.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Iterator;

/**
 *  The <tt>HopcroftKarp</tt> class represents a data type for computing a
 *  <em>maximum (cardinality) matching</em> and a
 *  <em>minimum (cardinality) vertex cover</em> in a bipartite graph.
 *  A <em>bipartite graph</em> in a graph whose vertices can be partitioned
 *  into two disjoint sets such that every edge has one endpoint in either set.
 *  A <em>matching</em> in a graph is a subset of its edges with no common
 *  vertices. A <em>maximum matching</em> is a matching with the maximum number
 *  of edges.
 *  A <em>perfect matching</em> is a matching which matches all vertices in the graph.
 *  A <em>vertex cover</em> in a graph is a subset of its vertices such that
 *  every edge is incident to at least one vertex. A <em>minimum vertex cover</em>
 *  is a vertex cover with the minimum number of vertices.
 *  By Konig's theorem, in any biparite
 *  graph, the maximum number of edges in matching equals the minimum number
 *  of vertices in a vertex cover.
 *  The maximum matching problem in <em>nonbipartite</em> graphs is
 *  also important, but all known algorithms for this more general problem
 *  are substantially more complicated.
 *  <p>
 *  This implementation uses the <em>Hopcroft-Karp algorithm</em>.
 *  The order of growth of the running time in the worst case is
 *  (<em>E</em> + <em>V</em>) sqrt(<em>V</em>),
 *  where <em>E</em> is the number of edges and <em>V</em> is the number
 *  of vertices in the graph. It uses extra space (not including the graph)
 *  proportional to <em>V</em>.
 *  <p>
 *  See also {@link BipartiteMatching}, which solves the problem in
 *  O(<em>E V</em>) time using the <em>alternating path algorithm</em>
 *  and <a href = "http://algs4.cs.princeton.edu/65reductions/BipartiteMatchingToMaxflow.java.html">BipartiteMatchingToMaxflow</a>,
 *  which solves the problem in O(<em>E V</em>) time via a reduction
 *  to the maxflow problem.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://algs4.cs.princeton.edu/65reductions">Section 6.5</a>
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class HopcroftKarp {
    private static final int UNMATCHED = -1;

    private final int V;                 // number of vertices in the graph
    private BipartiteX bipartition;      // the bipartition
    private int cardinality;             // cardinality of current matching
    private int[] mate;                  // mate[v] =  w if v-w is an edge in current matching
                                         //         = -1 if v is not in current matching
    private boolean[] inMinVertexCover;  // inMinVertexCover[v] = true iff v is in min vertex cover
    private boolean[] marked;            // marked[v] = true iff v is reachable via alternating path
    private int[] distTo;                // distTo[v] = number of edges on shortest path to v

    /**
     * Determines a maximum matching (and a minimum vertex cover)
     * in a bipartite graph.
     *
     * @param  G the bipartite graph
     * @throws IllegalArgumentException if <tt>G</tt> is not bipartite
     */
    public HopcroftKarp(Graph G) {
        bipartition = new BipartiteX(G);
        if (!bipartition.isBipartite()) {
            throw new IllegalArgumentException("graph is not bipartite");
        }

        // initialize empty matching
        this.V = G.V();
        mate = new int[V];
        for (int v = 0; v < V; v++)
            mate[v] = UNMATCHED;

        // the call to hasAugmentingPath() provides enough info to reconstruct level graph
        while (hasAugmentingPath(G)) {

            // to be able to iterate over each adjacency list, keeping track of which
            // vertex in each adjacency list needs to be explored next
            Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[G.V()];
            for (int v = 0; v < G.V(); v++)
                adj[v] = G.adj(v).iterator();

            // for each unmatched vertex s on one side of bipartition
            for (int s = 0; s < V; s++) {
                if (isMatched(s) || !bipartition.color(s)) continue;   // or use distTo[s] == 0

                // find augmenting path from s using nonrecursive DFS
                Stack<Integer> path = new Stack<Integer>();
                path.push(s);
                while (!path.isEmpty()) {
                    int v = path.peek();

                    // retreat, no more edges in level graph leaving v
                    if (!adj[v].hasNext())
                        path.pop();

                    // advance
                    else {
                        // process edge v-w only if it is an edge in level graph
                        int w = adj[v].next();
                        if (!isLevelGraphEdge(v, w)) continue;

                        // add w to augmenting path
                        path.push(w);

                        // augmenting path found: update the matching
                        if (!isMatched(w)) {
                            // StdOut.println("augmenting path: " + toString(path));

                            while (!path.isEmpty()) {
                                int x = path.pop();
                                int y = path.pop();
                                mate[x] = y;
                                mate[y] = x;
                            }
                            cardinality++;
                        }
                    }
                }
            }
        }

        // also find a min vertex cover
        inMinVertexCover = new boolean[V];
        for (int v = 0; v < V; v++) {
            if (bipartition.color(v) && !marked[v]) inMinVertexCover[v] = true;
            if (!bipartition.color(v) && marked[v]) inMinVertexCover[v] = true;
        }

        assert certifySolution(G);
    }

    // string representation of augmenting path (chop off last vertex)
    private static String toString(Iterable<Integer> path) {
        StringBuilder sb = new StringBuilder();
        for (int v : path)
            sb.append(v + "-");
        String s = sb.toString();
        s = s.substring(0, s.lastIndexOf('-'));
        return s;
    }

   // is the edge v-w in the level graph?
    private boolean isLevelGraphEdge(int v, int w) {
        return (distTo[w] == distTo[v] + 1) && isResidualGraphEdge(v, w);
    }

   // is the edge v-w a forward edge not in the matching or a reverse edge in the matching?
    private boolean isResidualGraphEdge(int v, int w) {
        if ((mate[v] != w) &&  bipartition.color(v)) return true;
        if ((mate[v] == w) && !bipartition.color(v)) return true;
        return false;
    }


    // is there an augmenting path?
    // an alternating path is a path whose edges belong alternately to the matching and not to the matchign
    // an augmenting path is an alternating path that starts and ends at unmatched vertices
    //
    // if so, upon termination adj[] contains the level graph;
    // if not, upon termination marked[] specifies those vertices reachable via an alternating path
    // from one side of the bipartition
    private boolean hasAugmentingPath(Graph G) {

        // shortest path distances
        marked = new boolean[V];
        distTo = new int[V];
        for (int v = 0; v < V; v++)
            distTo[v] = Integer.MAX_VALUE;

        // breadth-first search (starting from all unmatched vertices on one side of bipartition)
        Queue<Integer> queue = new Queue<Integer>();
        for (int v = 0; v < V; v++) {
            if (bipartition.color(v) && !isMatched(v)) {
                queue.enqueue(v);
                marked[v] = true;
                distTo[v] = 0;
            }
        }

        // run BFS until an augmenting path is found
        // (and keep going until all vertices at that distance are explored)
        boolean hasAugmentingPath = false;
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : G.adj(v)) {

                // forward edge not in matching or backwards edge in matching
                if (isResidualGraphEdge(v, w)) {
                    if (!marked[w]) {
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        if (!isMatched(w))
                            hasAugmentingPath = true;

                        // stop enqueuing vertices once an alternating path has been discovered
                        // (no vertex on same side will be marked if its shortest path distance longer)
                        if (!hasAugmentingPath) queue.enqueue(w);
                    }
                }
            }
        }

        return hasAugmentingPath;
    }

    /**
     * Returns the vertex to which the specified vertex is matched in
     * the maximum matching computed by the algorithm.
     *
     * @param  v the vertex
     * @return the vertex to which vertex <tt>v</tt> is matched in the
     *         maximum matching; <tt>-1</tt> if the vertex is not matched
     * @throws IllegalArgumentException unless <tt>0 &le; v &lt; V</tt>
     *
     */
    public int mate(int v) {
        validate(v);
        return mate[v];
    }

    /**
     * Returns true if the specified vertex is matched in the maximum matching
     * computed by the algorithm.
     *
     * @param  v the vertex
     * @return <tt>true</tt> if vertex <tt>v</tt> is matched in maximum matching;
     *         <tt>false</tt> otherwise
     * @throws IllegalArgumentException unless <tt>0 &le; v &lt; V</tt>
     *
     */
    public boolean isMatched(int v) {
        validate(v);
        return mate[v] != UNMATCHED;
    }

    /**
     * Returns the number of edges in any maximum matching.
     *
     * @return the number of edges in any maximum matching
     */
    public int size() {
        return cardinality;
    }

    /**
     * Returns true if the graph contains a perfect matching.
     * That is, the number of edges in a maximum matching is equal to one half
     * of the number of vertices in the graph (so that every vertex is matched).
     *
     * @return <tt>true</tt> if the graph contains a perfect matching;
     *         <tt>false</tt> otherwise
     */
    public boolean isPerfect() {
        return cardinality * 2 == V;
    }

    /**
     * Returns true if the specified vertex is in the minimum vertex cover
     * computed by the algorithm.
     *
     * @param  v the vertex
     * @return <tt>true</tt> if vertex <tt>v</tt> is in the minimum vertex cover;
     *         <tt>false</tt> otherwise
     * @throws IllegalArgumentException unless <tt>0 &le; v &lt; V</tt>
     */
    public boolean inMinVertexCover(int v) {
        validate(v);
        return inMinVertexCover[v];
    }

    // throw an exception if vertex is invalid
    private void validate(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**************************************************************************
     *   
     *  The code below is solely for testing correctness of the data type.
     *
     **************************************************************************/

    // check that mate[] and inVertexCover[] define a max matching and min vertex cover, respectively
    private boolean certifySolution(Graph G) {

        // check that mate(v) = w iff mate(w) = v
        for (int v = 0; v < V; v++) {
            if (mate(v) == -1) continue;
            if (mate(mate(v)) != v) return false;
        }

        // check that size() is consistent with mate()
        int matchedVertices = 0;
        for (int v = 0; v < V; v++) {
            if (mate(v) != -1) matchedVertices++;
        }
        if (2*size() != matchedVertices) return false;

        // check that size() is consistent with minVertexCover()
        int sizeOfMinVertexCover = 0;
        for (int v = 0; v < V; v++)
            if (inMinVertexCover(v)) sizeOfMinVertexCover++;
        if (size() != sizeOfMinVertexCover) return false;

        // check that mate() uses each vertex at most once
        boolean[] isMatched = new boolean[V];
        for (int v = 0; v < V; v++) {
            int w = mate[v];
            if (w == -1) continue;
            if (v == w) return false;
            if (v >= w) continue;
            if (isMatched[v] || isMatched[w]) return false;
            isMatched[v] = true;
            isMatched[w] = true;
        }

        // check that mate() uses only edges that appear in the graph
        for (int v = 0; v < V; v++) {
            if (mate(v) == -1) continue;
            boolean isEdge = false;
            for (int w : G.adj(v)) {
                if (mate(v) == w) isEdge = true;
            }
            if (!isEdge) return false;
        }

        // check that inMinVertexCover() is a vertex cover
        for (int v = 0; v < V; v++)
            for (int w : G.adj(v))
                if (!inMinVertexCover(v) && !inMinVertexCover(w)) return false;

        return true;
    }

    /** 
     * Unit tests the <tt>HopcroftKarp</tt> data type.   
     * Takes three command-line arguments <tt>V1</tt>, <tt>V2</tt>, and <tt>E</tt>;
     * creates a random bipartite graph with <tt>V1</tt> + <tt>V2</tt> vertices
     * and <tt>E</tt> edges; computes a maximum matching and minimum vertex cover;
     * and prints the results.
     */
    public static void main(String[] args) {

        int V1 = Integer.parseInt(args[0]);
        int V2 = Integer.parseInt(args[1]);
        int E  = Integer.parseInt(args[2]);
        Graph G = GraphGenerator.bipartite(V1, V2, E);
        if (G.V() < 1000) StdOut.println(G);

        HopcroftKarp matching = new HopcroftKarp(G);

        // print maximum matching
        StdOut.printf("Number of edges in max matching        = %d\n", matching.size());
        StdOut.printf("Number of vertices in min vertex cover = %d\n", matching.size());
        StdOut.printf("Graph has a perfect matching           = %b\n", matching.isPerfect());
        StdOut.println();

        if (G.V() >= 1000) return;

        StdOut.print("Max matching: ");
        for (int v = 0; v < G.V(); v++) {
            int w = matching.mate(v);
            if (matching.isMatched(v) && v < w)  // print each edge only once
                StdOut.print(v + "-" + w + " ");
        }
        StdOut.println();

        // print minimum vertex cover
        StdOut.print("Min vertex cover: ");
        for (int v = 0; v < G.V(); v++)
            if (matching.inMinVertexCover(v))
                StdOut.print(v + " ");
        StdOut.println();
    }

}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
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
