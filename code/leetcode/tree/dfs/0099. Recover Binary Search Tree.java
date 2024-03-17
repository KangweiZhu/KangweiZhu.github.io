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
  TreeNode first = null;
  TreeNode second = null;
  TreeNode previous = new TreeNode(Integer.MIN_VALUE);

  public void recoverTree(TreeNode root) {
      helper(root);
      int temp = first.val;
      first.val = second.val;
      second.val = temp;
  }

  public void helper(TreeNode current) {
      if (current == null) {
          return;
      }
      helper(current.left);
      if (first == null && current.val < previous.val) {
          first = previous;
      }
      if (first != null && current.val < previous.val) {
          second = current;
      }
      previous = current;
      helper(current.right);
  }
}



class Solution{

  public void recoverTree(TreeNode root) {
      Stack<Integer> vals = new Stack<>();
      Stack<TreeNode> treeNodes = new Stack<>();
      helper(current, val, treeNodes);
  }

  public void helper(TreeNode current, Stack<Integer> vals, Stack<TreeNode> treeNodes) {
    if (current == null) {
      return null;
    }
    helper(current.left, vals, treeNodes);
    
    helper(current.right, vals, treeNodes);
  }
}