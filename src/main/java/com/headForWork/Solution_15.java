package com.headForWork;

import org.junit.Assert;
import org.junit.Test;

/**
 * 面试题15 ：二进制中1 的个数
 */
public class Solution_15 {
    public static int NumberOf1(int n){
        int result=0;
        int i=1;
        while(i!=0){
            if((n & i)!=0)
                result++;
            i<<=1;

        }
        return result;
    }
    @Test
    public void test(){
        System.out.println(NumberOf1(3));
    }
}
