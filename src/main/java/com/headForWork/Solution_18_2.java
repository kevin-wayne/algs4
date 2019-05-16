package com.headForWork;

import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.LinkedList;

/**
 * 删除链表中重复的节点
 * 在一个排序的链表中，如何删除重复的节点？
 *
 */
public class Solution_18_2 {
    class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
        ListNode(int val,ListNode next){
            this.next=next;
            this.val=val;
        }
    }
        public ListNode deleteDuplication(ListNode pHead){
            if(pHead==null||pHead.next==null){
                return pHead;
            }
            if(pHead.val==pHead.next.val){
                if(pHead.next.next==null){
                    return null;
                }
                if(pHead.next.next.val==pHead.val){
                    return deleteDuplication(pHead.next);
                }
                return deleteDuplication(pHead.next.next);
            }
            pHead.next=deleteDuplication(pHead.next);
            return pHead;

        }
        public void print(ListNode node){
            System.out.println(node.val);
            if(node.next!=null){
                print(node.next);
            }
        }
    @Test
    public void test(){
        ListNode l1=new ListNode(1);
        ListNode l2=new ListNode(2);
        ListNode l3=new ListNode(2);
        ListNode l4=new ListNode(2);
        ListNode l5=new ListNode(3);
        ListNode l6=new ListNode(1);
        l1.next=l2;
        l2.next=l3;
        l3.next=l4;
        l4.next=l5;
        l5.next=l6;
        l6.next=null;
        deleteDuplication(l1);
        print(l1);

    }

    }
