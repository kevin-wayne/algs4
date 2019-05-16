package com.headForWork;

import org.junit.Test;

/**
 * 面试题17： 打印从1 到最大的n位数
 * 题目： 输入数字n, 按顺便打印出从1到最大的n 位十进制数
 * eg ; 输入n=3
 *      打印:1,2,3,.....999
 * 陷阱：面试官没有给出n 的范围，当输入的n 很大的时候，
 * 我们求最大的n 位数是不是用整型或者长整型long long 都会溢出
 * 我们应该考虑大数问题。
 * 我们需要在字符串上模拟数字加法的解法
 */
public class Solution_17 {
    /**
     * 打印从1 到最大的n位数
     * @param n n 位
     */
    public void printToMaxOfNDigits(int n){
        if(n<1) {
            return;
        }

        char[] chars=new char[n];

        for(int i=0;i<n;++i)
            chars[i]='0';
        while (!increment(chars))
            printNumber(chars);
    }

    /**
     * 打印数字（去除前面的0）
     * @param chars 数字数组
     */
    private void printNumber(char[] chars){
        int index=0;
        int n=chars.length;
        for(char ch:chars) {
            if (ch != '0') {
                break;
            }
            ++index;
        }
        StringBuffer sb=new StringBuffer();
        for(int i=index;i<n;++i)
            sb.append(chars[i]);
        System.out.println(sb.toString());
    }

    /**
     * 数字加一
     * @param chars 数字数组*
     * @return 是否溢出
     */
    private boolean increment(char[] chars){
        boolean flag=false;
        int n=chars.length;
        int carry=1;
        for(int i=n-1;i>=0;--i){
            int num=chars[i]-'0'+carry;
            if(num>9) {
                if (i == 0) {
                    flag = true;
                    break;
                }
                chars[i] = '0';
            }else {
                ++chars[i];
                break;
            }
        }
        return flag;
    }

    public static void main(String[] args){
        char char1='1';
        System.out.println(++char1);
        Solution_17 sol=new Solution_17();
        sol.printToMaxOfNDigits(2);

    }

}
