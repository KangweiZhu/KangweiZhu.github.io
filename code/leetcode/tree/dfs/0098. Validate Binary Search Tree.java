/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean validate(TreeNode current, Long min, Long max) {
        if (current == null) {
            return true;
        }
        long currentVal = current.val;
        if (currentVal <= min || currentVal >= max) {
            return false;
        }
        boolean flag1 = validate(current.left, min, currentVal);
        boolean flag2 = validate(current.right, currentVal, max);
        return flag1 && flag2;
    }
}