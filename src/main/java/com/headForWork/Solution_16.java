package com.headForWork;

import org.junit.Test;

/**
 *树值的整数次方
 * 实现double Power(double base,int exponent) 求base 的exponent 次方，不能使用库函数，同时不考虑大数问题
 */
public class Solution_16 {
    public double solution(double base,int exponent){

        double result=1;
        int n=Math.abs(exponent);
        for(int i=0;i<n;i++){
            result*=base;
        }
        return exponent<0?1/result:result;
    }
    @Test
    public void test(){
        System.out.println(solution(2.4,2));
    }
}
