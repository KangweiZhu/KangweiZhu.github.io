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
    Map<Integer, Integer> inorderMap = new HashMap<>();
    int preOrderIndex = 0;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            inorderMap.put(inorder[i], i);
        }  
        TreeNode root = helper(preorder, 0, preorder.length - 1);
        return root;
    }

    public TreeNode helper(int[] preorder, int left, int right) {
        if (left > right) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[preOrderIndex]);
        preOrderIndex++;
        root.left = helper(preorder, left, inorderMap.get(root.val) - 1);
        root.right = helper(preorder, inorderMap.get(root.val) + 1, right);
        return root;
    }
}

/**
 * 
 * 这题花了我加起来1个小时。。菜笑了。
 * Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
   Output: [3,9,20,null,null,15,7]
 * 
 * 首先我们要知道几个规律:
 *      1. 前序遍历中，开头的第一个，永远都是根节点。
 *      2. 中序遍历中，开头的永远都是最左边的。或根节点。
 *      3. 前序遍历中，前三个， 要么是 根-根左子-根左左子 ， 要么是 根-根左子-根右子
 *      4. 中序遍历中，左节点的index 永远在 根节点的index 的右边。 右节点的index 永远在 根节点的index 的左边。
 * 
 * Map<Integer, Integer> inorderMap = new HashMap<>() 的作用是创建值-index对。通过preOrder的值，拿在inorder中的下标。
 * 
 * 对于每个在preOrder中的节点，我们都把他看成独立的节点，因为他有可能会有左右子节点。
 * 
 * 那么怎么样才会停下来呢，怎么判断当前的节点有没有左右子节点呢？
 * 这就得看上面总结的规律了。
 * 
 * 如果他是根节点，那他的左节点首先必须在左边界内，并处于根节点-1（自身就是边界）的位置上，也就是右边界。
 * 他的右节点同样必须在左边界内，也就是根节点-1（自身就是边界）；并处于右边界内。
 * 
 * 记住：根节点的左边，把边界限制在右，因为根节点的左节点可能还有左节点（数组同理，即左边可能还有，但是自身已经锁死了，右侧不可能有东西，右边的东西必然是上一级的右节点。自身是上一级的左节点）. 根节点的右边，把边界限制在左边，因为右节点的右边可能还有右节点。
 * 这个规则仅限于 中序排列 后的数组。因为这是inorder traverse的特性。还不懂的自己画图
 * 
 *              
 *  */     

export all_proxy="socks5://127.0.0.1:7890

~/.bashrc
~/.zshrc
source this two 
curl -vvvk https://www.google.com
//set rule
curl cip.cc
//set global
curl cip.cc  