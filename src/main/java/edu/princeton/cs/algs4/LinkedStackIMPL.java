package edu.princeton.cs.algs4;

public class LinkedStackIMPL<Item> {
    protected int n;          // size of the stack
    protected Node first;     // top of stack

    public LinkedStackIMPL(){
        n = 0;
        first = null;
    }

    // check internal invariants
    protected boolean check() {

        // check a few properties of instance variable 'first'
        if (n < 0) {
            return false;
        }
        if (n == 0) {
            if (first != null) return false;
        }
        else if (n == 1) {
            if (first == null)      return false;
            if (first.next != null) return false;
        }
        else {
            if (first == null)      return false;
            if (first.next == null) return false;
        }

        // check internal consistency of instance variable n
        int numberOfNodes = 0;
        for (Node x = first; x != null && numberOfNodes <= n; x = x.next) {
            numberOfNodes++;
        }
        if (numberOfNodes != n) return false;

        return true;
    }

    // helper linked list class
    protected class Node {
        protected Item item;
        protected LinkedStack.Node next;
    }
}
