package com.leetcode;

/* -----------------------------------
 *  WARNING:
 * -----------------------------------
 *  Your code may fail to compile
 *  because it contains public class
 *  declarations.
 *  To fix this, please remove the
 *  "public" keyword from your class
 *  declarations.
 */

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import static java.lang.Math.pow;

class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ArrayList<Integer> L1=new ArrayList<>();
        ArrayList<Integer> L2=new ArrayList<>();
        do {
            L1.add(l1.val);
            l1=l1.next;

        }
        while (l2.next!=null);
        do {
            L2.add(l2.val);
            l2=l2.next;

        }
        while (l2.next!=null);
        double l1Int=0;double l2Int=0;
        for(int i=0;i<L1.size();i++){
            l1Int=l1Int+pow(L1.get(i),10);
        }
        for(int i=0;i<L2.size();i++){
            l2Int=l2Int+pow(10,L2.get(i));
        }
        ListNode result=null;
        ListNode temp2=null,temp3=null;
        double temp=l1Int+l2Int;
        String temp1=Double.toString(temp);
        for(int i=0;i<(temp/10+1);i++){

            temp2=new ListNode(temp1.charAt(i));
            if(i==0)
                temp2.next=null;
            else
                temp2.next=temp3;
            temp3=temp2;
        }
        result=temp2;
        return result;

    }
}

public class MainClass {
    public static int[] stringToIntegerArray(String input) {
        input = input.trim();
        input = input.substring(1, input.length() - 1);
        if (input.length() == 0) {
            return new int[0];
        }

        String[] parts = input.split(",");
        int[] output = new int[parts.length];
        for(int index = 0; index < parts.length; index++) {
            String part = parts[index].trim();
            output[index] = Integer.parseInt(part);
        }
        return output;
    }

    public static ListNode stringToListNode(String input) {
        // Generate array from the input
        int[] nodeValues = stringToIntegerArray(input);

        // Now convert that list into linked list
        ListNode dummyRoot = new ListNode(0);
        ListNode ptr = dummyRoot;
        for(int item : nodeValues) {
            ptr.next = new ListNode(item);
            ptr = ptr.next;
        }
        return dummyRoot.next;
    }

    public static String listNodeToString(ListNode node) {
        if (node == null) {
            return "[]";
        }

        String result = "";
        while (node != null) {
            result += Integer.toString(node.val) + ", ";
            node = node.next;
        }
        return "[" + result.substring(0, result.length() - 2) + "]";
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            ListNode l1 = stringToListNode(line);
            line = in.readLine();
            ListNode l2 = stringToListNode(line);

            ListNode ret = new Solution().addTwoNumbers(l1, l2);

            String out = listNodeToString(ret);

            System.out.print(out);
        }
    }
}