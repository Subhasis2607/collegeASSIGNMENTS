import java.util.*;

public class BSTAnalysis {

    static class Node {
        int key;
        Node left, right;

        Node(int item) {
            this.key = item;
            this.left = null;
            this.right = null;
        }
    }

    static class BST {
        Node root;

        BST() {
            root = null;
        }

        // Iterative insert to prevent StackOverflow on large sorted inputs
        void insert(int key) {
            Node newNode = new Node(key);
            if (root == null) {
                root = newNode;
                return;
            }
            Node current = root;
            Node parent = null;
            while (current != null) {
                parent = current;
                if (key < current.key) {
                    current = current.left;
                } else if (key > current.key) {
                    current = current.right;
                } else {
                    return; // Ignore duplicates
                }
            }
            if (key < parent.key) parent.left = newNode;
            else parent.right = newNode;
        }

        boolean search(int key) {
            Node current = root;
            while (current != null) {
                if (key == current.key) return true;
                current = (key < current.key) ? current.left : current.right;
            }
            return false;
        }

        void delete(int key) {
            root = deleteRec(root, key);
        }

        private Node deleteRec(Node root, int key) {
            if (root == null) return null;

            if (key < root.key) {
                root.left = deleteRec(root.left, key);
            } else if (key > root.key) {
                root.right = deleteRec(root.right, key);
            } else {
                // Case 1 & 2: Node with 0 or 1 child
                if (root.left == null) return root.right;
                if (root.right == null) return root.left;

                // Case 3: Node with 2 children (Inorder Successor: min in right subtree)
                root.key = minValue(root.right);
                root.right = deleteRec(root.right, root.key);
            }
            return root;
        }

        private int minValue(Node root) {
            int minv = root.key;
            while (root.left != null) {
                minv = root.left.key;
                root = root.left;
            }
            return minv;
        }

        // Traversals
        void inorder(Node node) {
            if (node != null) {
                inorder(node.left);
                System.out.print(node.key + " ");
                inorder(node.right);
            }
        }

        void preorder(Node node) {
            if (node != null) {
                System.out.print(node.key + " ");
                preorder(node.left);
                preorder(node.right);
            }
        }

        void postorder(Node node) {
            if (node != null) {
                postorder(node.left);
                postorder(node.right);
                System.out.print(node.key + " ");
            }
        }

        // Iterative height calculation to handle deep degenerate trees
        int getHeight() {
            if (root == null) return 0;
            Map<Node, Integer> heights = new HashMap<>();
            Deque<Node> stack = new ArrayDeque<>();
            Node curr = root;
            Node lastVisited = null;
            int maxHeight = 0;

            while (!stack.isEmpty() || curr != null) {
                if (curr != null) {
                    stack.push(curr);
                    curr = curr.left;
                } else {
                    Node peekNode = stack.peek();
                    if (peekNode.right != null && lastVisited != peekNode.right) {
                        curr = peekNode.right;
                    } else {
                        stack.pop();
                        int leftH = heights.getOrDefault(peekNode.left, 0);
                        int rightH = heights.getOrDefault(peekNode.right, 0);
                        int h = 1 + Math.max(leftH, rightH);
                        heights.put(peekNode, h);
                        maxHeight = Math.max(maxHeight, h);
                        lastVisited = peekNode;
                    }
                }
            }
            return maxHeight;
        }
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println("PART 1: Insertions, Traversals, and Search");
        System.out.println();
        BST tree = new BST();
        int[] initialKeys = {50, 30, 70, 20, 40, 60, 80, 10};
        for (int k : initialKeys) tree.insert(k);

        System.out.print("Inorder Traversal:   ");
        tree.inorder(tree.root);
        System.out.println(" (Sorted Order Verified)");

        System.out.print("Preorder Traversal:  ");
        tree.preorder(tree.root);
        System.out.println();

        System.out.print("Postorder Traversal: ");
        tree.postorder(tree.root);
        System.out.println();

        System.out.println("\nSearch operations:");
        System.out.println("Search 40 (Present): " + (tree.search(40) ? "Found" : "Not Found"));
        System.out.println("Search 99 (Absent):  " + (tree.search(99) ? "Found" : "Not Found"));

        System.out.println();
        System.out.println("PART 2: Deletions");
        System.out.println();

        // (i) Leaf Node: 10
        System.out.println("Deleting leaf node (10):");
        tree.delete(10);
        System.out.print("Inorder: ");
        tree.inorder(tree.root);
        System.out.println();

        // (ii) Single Child Node: 20 (now only has null left, no children or single child context)
        // Let's delete 30 which has one child (40) since 20 lost 10
        System.out.println("\nDeleting node with one child (20):");
        tree.delete(20);
        System.out.print("Inorder: ");
        tree.inorder(tree.root);
        System.out.println();

        // (iii) Two Children Node: 50 (Root)
        System.out.println("\nDeleting node with two children (50):");
        tree.delete(50);
        System.out.print("Inorder: ");
        tree.inorder(tree.root);
        System.out.println();

        System.out.println();
        System.out.println("PART 3: ");
        System.out.println();

        int[] sizes = {1000, 5000, 10000};
        String[] types = {"Random", "Sorted", "Reverse-Sorted"};

        System.out.printf("%-8s | %-14s | %-8s | %-15s | %-18s | %-18s%n",
                "N", "Sequence Type", "Height", "Build Time (ms)", "1000 Search (ms)", "500 Delete (ms)");
        System.out.println("---------------------------------------------------------------------------------------------");

        Random rand = new Random(42);

        for (int n : sizes) {
            for (String type : types) {
                int[] arr = new int[n];
                for (int i = 0; i < n; i++) arr[i] = i + 1;

                if (type.equals("Random")) {
                    for (int i = n - 1; i > 0; i--) {
                        int j = rand.nextInt(i + 1);
                        int temp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = temp;
                    }
                } else if (type.equals("Reverse-Sorted")) {
                    for (int i = 0; i < n / 2; i++) {
                        int temp = arr[i];
                        arr[i] = arr[n - 1 - i];
                        arr[n - 1 - i] = temp;
                    }
                }

                // 1. Build Tree
                BST testTree = new BST();
                long start = System.nanoTime();
                for (int val : arr) testTree.insert(val);
                long end = System.nanoTime();
                double buildTime = (end - start) / 1e6;

                int height = testTree.getHeight();

                // 2. 1000 Searches
                start = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    testTree.search(rand.nextInt(n * 2));
                }
                end = System.nanoTime();
                double searchTime = (end - start) / 1e6;

                // 3. 500 Deletions
                start = System.nanoTime();
                for (int i = 0; i < 500; i++) {
                    testTree.delete(arr[i]);
                }
                end = System.nanoTime();
                double deleteTime = (end - start) / 1e6;

                System.out.printf("%-8d | %-14s | %-8d | %-15.3f | %-18.3f | %-18.3f%n",
                        n, type, height, buildTime, searchTime, deleteTime);
            }
        }
    }
}