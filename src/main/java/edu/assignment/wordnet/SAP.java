/******************************************************************************
 *  Compilation:  javac SAP.java
 *  Execution:    java edu.assignment.wordnet.SAP
 *  Dependencies: BreadthFirstDirectedPaths.java Digraph.java
 *  Data files:   src/main/resources/data/wordnet
 *
 *  Implements an immutable data type SAP.
 ******************************************************************************/

package edu.assignment.wordnet;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

/**
 * This class searches a shortest ancestral path.
 * <p>
 * For additional documentation,
 * see <a href="http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html">
 * Programming Assignment 1</a> of Coursera, Algorithms Part II
 *
 *  @author vahbuna
 */
public class SAP {

    private final Digraph dag;

    private int lastAncestor = -1;
    /**
     * constructor takes a digraph (not necessarily a DAG).
     * @param input input digraph
     */
    public SAP(final Digraph input) {
        dag = new Digraph(input);
    }

    /**
     * length of shortest ancestral path between v and w; -1 if no such path.
     * @param v vertex
     * @param w vertex
     * @return sap length
     */
    public int length(final int v, final int w) {
        if (v == w) {
            lastAncestor = v;
            return 0;
        }
        BreadthFirstDirectedPaths bfsPathsV =
                new BreadthFirstDirectedPaths(dag, v);
        BreadthFirstDirectedPaths bfsPathsW =
                new BreadthFirstDirectedPaths(dag, w);

        int sapLength = Integer.MAX_VALUE;
        lastAncestor = -1;
        for (int node = 0; node < dag.V(); node++) {
            if (dag.indegree(node) > 1) {
                boolean pathsV = bfsPathsV.hasPathTo(node);

                boolean pathsW = bfsPathsW.hasPathTo(node);
                // Iterator<Integer> pathsV = bfsPathsV.pathTo(node).iterator();
                // Iterator<Integer> pathsW = bfsPathsW.pathTo(node).iterator();
                if (pathsV && pathsW) {
                    int newPathLength = bfsPathsV.distTo(node)
                            + bfsPathsW.distTo(node);
                    if (newPathLength < sapLength) {
                        sapLength = newPathLength;
                        lastAncestor = node;
                    }
                }
            }
        }

        if (bfsPathsV.hasPathTo(w)) {
            int dist = bfsPathsV.distTo(w);
            if (dist < sapLength) {
                sapLength = dist;
                lastAncestor = w;
            }
        }

        if (bfsPathsW.hasPathTo(v)) {
            int dist = bfsPathsW.distTo(v);
            if (dist < sapLength) {
                sapLength = dist;
                lastAncestor = v;
            }
        }

        if (sapLength == Integer.MAX_VALUE) {
            return -1;
        }
        return sapLength;
    }

   /**
    * a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path.
    * @param v vertex
    * @param w vertex
    * @return common ancestor
    */
    public int ancestor(final int v, final int w) {
        length(v, w);
        return lastAncestor;
    }

    /**
     * length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path.
     * @param v vertex
     * @param w vertex
     * @return sap length
     */
    public int length(final Iterable<Integer> v, final Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException();
        }
        int sap = Integer.MAX_VALUE;
        for (Integer nodeV : v) {
            if (nodeV == null) {
                throw new java.lang.IllegalArgumentException();
            }
            for (Integer nodeW : w) {
                if (nodeW == null) {
                    throw new java.lang.IllegalArgumentException();
                }
                int tempPath = length(nodeV, nodeW);
                if (tempPath < sap) {
                    sap = tempPath;
                }
            }
        }
        return sap == Integer.MAX_VALUE ? -1 : sap;
    }

    /**
     * a common ancestor that participates in shortest ancestral path; -1 if no such path.
     * @param v vertex
     * @param w vertex
     * @return common ancestor
     */
    public int ancestor(final Iterable<Integer> v, final Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException();
        }
        int sap = Integer.MAX_VALUE;
        int ancestor = -1;
        for (Integer nodeV : v) {
            if (nodeV == null) {
                throw new java.lang.IllegalArgumentException();
            }
            for (Integer nodeW : w) {
                if (nodeW == null) {
                    throw new java.lang.IllegalArgumentException();
                }
                int tempPath = length(nodeV, nodeW);
                if (tempPath < sap) {
                    sap = tempPath;
                    ancestor = lastAncestor;
                }
            }
        }
        return ancestor;
    }
}
