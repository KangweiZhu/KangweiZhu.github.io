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
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stk = new Stack<>();
        TreeNode current = root;   

        while (current != null || !stk.isEmpty()) {
            while (current != null) {
                stk.push(current);
                current = current.left;
            }
            current = stk.pop();
            res.add(current.val);
            current = current.right;
        }

        return res;
    }
}

/** 递归法
class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorder(res, root);
        return res;
    }   

    public void inorder(List<Integer> res, TreeNode current) {
        if (current == null) {
            return;
        }
        inorder(res, current.left);
        res.add(current.val);
        inorder(res, current.right);
    }
}
*/