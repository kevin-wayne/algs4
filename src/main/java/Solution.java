public class Solution {
    public int[] twoSum(int[] nums,int target){
        int []index={0,0};
        int i ,j;
        for(i=0;i<nums.length;i++)
            for(j=i+1;j<nums.length;j++){
            int a=target-nums[i];
            if(nums[j]==a){
                index[0]=i;
                index[1]=j;
                return index;
            }
            }

        return index;
    }
    public int numJewelsInStones(String J,String S){
        int num=0;
        for(int j=0;j<J.length();j++)
            for(int i=0;i<S.length();i++){
            if(J.charAt(j)==S.charAt(i))
                num++;
            }
            return num;
    }
    public static void main(String args[]){
        int [] nums={21,2,7,11,15};
        int target=9;
        Solution solution=new Solution();
        int [] index=solution.twoSum(nums,target);
        for(int i: index)
        System.out.println(i);
    }
    public String toLowerCase(String str){
        return str.toLowerCase();
    }
}
