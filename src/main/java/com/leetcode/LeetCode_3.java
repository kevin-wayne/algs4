package com.leetcode;

import java.util.HashSet;
import java.util.Set;

public class LeetCode_3 {
    public int numJewelsInStones(String J,String S){
//        char[]charJ=J.toCharArray();
//        char[] charS=S.toCharArray();
//        int result=0;
//        for(int i=0;i<charJ.length;i++){
//            for(int j=0;j<charS.length;j++){
//                if(charJ[i]==charS[j])
//                    result=result+1;
//            }
//        }
//        System.out.println(result);
//        return result;
        int res = 0;
        Set setJ = new HashSet();
        for (char j: J.toCharArray()) setJ.add(j);
        for (char s: S.toCharArray()) if (setJ.contains(s)) res++;
        return res;

    }
    public static void main(String[] args){
        String J="aA";
        String S="aAAbbbb";
        LeetCode_3 problem=new LeetCode_3();
        problem.numJewelsInStones(J,S);


    }
}
