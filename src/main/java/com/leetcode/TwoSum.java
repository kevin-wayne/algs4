package com.leetcode;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        int[] temp = new int[2];
        for (int i = 0; i < nums.length; i++)
            for (int j = 1; j < nums.length - 1; j++)
                if (nums[i] + nums[j] == target) {
                    temp[0] = i;
                    temp[1] = j;
                }
        return temp;
    }

//    public int[] twoSum2(int[] nums, int target) {
//        Map<Integer, Integer> map = new HashMap<>();
//        for (int i = 0; i < nums.length; i++) {
//            map.put(nums[i], i);
//        }
////        for(int i=0;i<nums.length;i++){
////            int complement =target-nums[i];
////            if(map.containsKey())
////        }
//
//}
}
