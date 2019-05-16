package com.headForWork;

/**
 * 删除链表的节点
 * 给定单向链表的头指针和一个节点指针，定义一个在O（yi）时间内删除该节点。
 */
public class Solution_18 {
    class ListNode{
        int val;
        ListNode next;
    }

    /**
     * @param head
     * @param toBDelete
     * @return
     */
    public ListNode deleteNode(ListNode head,ListNode toBDelete){
        if(head==null|| toBDelete==null)
            return head;
        //删除的不是尾节点
        if(toBDelete.next!=null){
            toBDelete.val=toBDelete.next.val;
            toBDelete.next=toBDelete.next.next;
        }else if(head==toBDelete){
            head=null;
        }else{
            //删除的是为节点
            toBDelete=null;
        }
        return head;
    }
}
