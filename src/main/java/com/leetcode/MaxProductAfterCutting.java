package com.leetcode;

public class MaxProductAfterCutting {


    /**
     * 剪绳子求最大乘积
     * @param length 绳子长度
     * @return 乘积最大值
     */
    public int maxProductAfterCutting(int length) {
        if (length < 2) {
            return 0;
        }
        if (length < 4) {
            return length - 1;
        }

        // res[i] 表示当长度为i时的最大乘积
        int[] res = new int[length + 1];
        res[1] = 1;
        res[2] = 2;
        res[3] = 3;
        // 从长度为4开始计算
        for (int i = 4; i <= length; ++i) {
            int max = 0;
            for (int j = 1; j <= i / 2; ++j) {
                max = Math.max(max, res[j] * res[i - j]);
            }
            res[i] = max;
        }

        return res[length];

    }
    public static void main(String [] args){
        MaxProductAfterCutting temp=new MaxProductAfterCutting();
        System.out.println(temp.maxProductAfterCutting(3));
    }
}