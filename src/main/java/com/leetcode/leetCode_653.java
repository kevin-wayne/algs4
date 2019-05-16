package com.leetcode;

import java.util.LinkedList;
import java.util.List;

public class leetCode_653 {

    static List list;

    static class TreeNode{
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x){this.val=x;}
    }
    static boolean findTarget(TreeNode root,int k){
        list=new LinkedList();
        find(root, k);
        for(int i=0;i<list.size();i++){
            int sum=0;
            for(int j=1;j<list.size()-1;j++){
                sum=list.indexOf(i)+list.indexOf(j);
                if(sum==k)
                    return true;
            }
        }
        return false;
    }
    private static void find(TreeNode node, int k){
        if(node!=null){
            if(node.val<=k){
                list.add(node.val);
            }
            find(node.left,k);
        }
    }
    public static void main(String args[]){
        TreeNode root=new TreeNode(5);
        TreeNode node1=new TreeNode(3);
        TreeNode node2=new TreeNode(6);
        TreeNode node4=new TreeNode(2);
        TreeNode node5=new TreeNode(4);
        TreeNode node6=new TreeNode(7);
        root.left=node1;
        root.right=node2;
        node1.left=node4;
        node1.right=node5;
        node4.right=node6;
        System.out.println(findTarget(root,9));
    }
}
