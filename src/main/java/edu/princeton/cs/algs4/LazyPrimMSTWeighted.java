package edu.princeton.cs.algs4;

public class LazyPrimMSTWeighted {
    // edges in the MST
    protected Queue<Edge> mst;

    public LazyPrimMSTWeighted(){
        mst=new Queue<Edge>();
    }

    public Iterable<Edge> edges(){
        /**
         * Returns the edges in a minimum spanning tree (or forest).
         *
         * @return the edges in a minimum spanning tree (or forest) as
         * an iterable of edges
         */
        return mst;
    }
}
