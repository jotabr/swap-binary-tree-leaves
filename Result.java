import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

// https://www.hackerrank.com/challenges/swap-nodes-algo/problem
class Node<T extends Comparable<?>> {
    Node<T> left;
    Node<T> right;
    T data;

    public Node(T data) {
        this.data = data;
    }
}

class Result {

    /*
     * Complete the 'swapNodes' function below.
     *
     * The function is expected to return a 2D_INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. 2D_INTEGER_ARRAY indexes
     *  2. INTEGER_ARRAY queries
     */
    static int nodesCounter = 0;

    public static List<List<Integer>> swapNodes(List<List<Integer>> indexes, List<Integer> queries) {
        // Write your code here
        Node<Integer> rootNode = createTree(indexes);
        int[][] result = new int[queries.size()][nodesCounter];

        for (int i = 0; i < queries.size(); i++) {
            swapLevel(rootNode, queries.get(i));
            int[] arr = new int[nodesCounter];
            writeToArray(rootNode, arr, new StaticIntHolder());
            result[i] = arr;
        }

        BTreePrinter.printNode(rootNode);

        return Arrays
                .stream(result)
                .map(
                        internalArray ->
                                Arrays
                                        .stream(internalArray)
                                        .boxed()
                                        .collect(Collectors.toList())
                )
                .collect(
                        Collectors.toList()
                );
    }

    static void swapLevel(Node root, int level) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int currLevel = 1;
        int size = queue.size();

        while (!queue.isEmpty()) {
            Node n = queue.remove();

            if (currLevel % level == 0) {
                Node temp = n.left;
                n.left = n.right;
                n.right = temp;
            }
            if (n.left != null) {
                queue.add(n.left);
            }
            if (n.right != null) {
                queue.add(n.right);
            }
            size--;
            if (size == 0) {
                currLevel++;
                size = queue.size();
            }
        }
    }

    static void writeToArray(Node root, int[] arr, StaticIntHolder intHolder) {
        if (root == null) {
            return;
        }
        writeToArray(root.left, arr, intHolder);
        arr[intHolder.val++] = (Integer)root.data;
        writeToArray(root.right, arr, intHolder);
    }

    static class StaticIntHolder {
        public int val;
    }

    static Node createTree(List<List<Integer>> indexes) {
        Node rootNode = new Node(1);
        Queue<Node> queue = new LinkedList<>();
        queue.add(rootNode);

        for (List<Integer> index : indexes) {
            if (!queue.isEmpty()) {
                Node n = queue.remove();
                nodesCounter++;
                if (index.get(0) != -1) {
                    n.left = new Node(index.get(0));
                    queue.add(n.left);
                }
                if (index.get(1) != -1) {
                    n.right = new Node(index.get(1));
                    queue.add(n.right);
                }
            }
        }
        return rootNode;
    }

}

class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<Integer>> indexes = new ArrayList<>();

        IntStream.range(0, n).forEach(i -> {
            try {
                indexes.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        int queriesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> queries = IntStream.range(0, queriesCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine().replaceAll("\\s+$", "");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(toList());

        List<List<Integer>> result = Result.swapNodes(indexes, queries);

        result.stream()
                .map(
                        r -> r.stream()
                                .map(Object::toString)
                                .collect(joining(" "))
                )
                .map(r -> r + "\n")
                .collect(toList())
                .forEach(e -> {
                    try {
                        bufferedWriter.write(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
