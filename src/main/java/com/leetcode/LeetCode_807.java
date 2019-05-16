package com.leetcode;

public class LeetCode_807 {

    public int maxIncreaseKeepingSkuline(int [][] grid){
        int[] topView=new int[grid[1].length];
        int[] leftView=new int [grid.length];
        for(int i=0;i<topView.length;i++){
            topView[i]=0;
        }
        for(int i=0;i<leftView.length;i++){
            leftView[i]=0;
        }
        //first  get skyine viewed from top to bottom
        for (int i=0;i<grid[1].length;i++)
            for (int j=0;j<grid.length;j++){
                if(grid[i][j]>topView[i])
                    topView[i]=grid[i][j];
                if(grid[i][j]>leftView[j])
                    leftView[j]=grid[i][j];
            }
        int[][]topViewSkyine=new int[topView.length][grid.length];
        for(int i=0;i<topView.length;i++){
            for(int j=0;j<grid.length;j++){
                topViewSkyine[i][j]=topView[i];
            }
        }
        for(int i=0;i<topViewSkyine[1].length;i++){
            for(int j=0;j<topViewSkyine.length;j++){
                if(topViewSkyine[i][j]>leftView[j])
                    topViewSkyine[i][j]=leftView[j];
            }
        }
        int sum=0;
        for(int i=0;i<topViewSkyine[1].length;i++){
            for(int j=0;j<topViewSkyine.length;j++){
                int temp=topViewSkyine[i][j]-grid[i][j];
                sum+=temp;
            }
        }
        return sum;
    }


}
