package anaydis.search;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Tomas Perez Molina
 */
public class RandomizedTreeMap<K, V> implements Map<K,V> {
    private Node head;
    private int size;
    private final Comparator<K> keyComp;
    private Random random = new Random();

    public RandomizedTreeMap(Comparator<K> keyComparator) {
        keyComp = keyComparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(@NotNull K key) {
        return find(head, key) != null;
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        final Node node = find(head, key);
        if(node != null) return node.value;
        else return null;
    }

    @Override
    public V put(@NotNull K key, V value) {
        NodeValuePair putPair;
        if(random.nextBoolean()) {
            putPair = put(head, key, value);
        } else {
            putPair = rootPut(head, key, value);
        }
        head = putPair.node;
        return putPair.value;
    }

    NodeValuePair put(@Nullable Node node, K key, V value){
        V prevValue = null;
        Node resultNode;
        if(node == null){
            size++;
            resultNode = new Node(key, value);
        }
        else {
            int comp = keyComp.compare(key, node.key);
            if(comp > 0){
                NodeValuePair pair = put(node.right, key, value);
                node.right = pair.node;
                prevValue = pair.value;
            }
            else if(comp < 0){
                NodeValuePair pair = put(node.left, key, value);
                node.left = pair.node;
                prevValue = pair.value;
            }
            else {
                prevValue = node.value;
                node.value = value;
            }
            resultNode = node;
        }
        return new NodeValuePair(resultNode, prevValue);
    }

    NodeValuePair rootPut(@Nullable Node node, K key, V value){
        V prevValue = null;
        Node resultNode;
        if(node == null){
            size++;
            resultNode = new Node(key, value);
        }
        else {
            int comp = keyComp.compare(key, node.key);
            if(comp > 0){
                NodeValuePair pair = put(node.right, key, value);
                node.right = pair.node;
                resultNode = rotateLeft(node);
                prevValue = pair.value;
            }
            else if(comp < 0){
                NodeValuePair pair = put(node.left, key, value);
                node.left = pair.node;
                resultNode = rotateRight(node);
                prevValue = pair.value;
            }
            else {
                prevValue = node.value;
                node.value = value;
                resultNode = node;
            }
        }
        return new NodeValuePair(resultNode, prevValue);
    }

    private Node rotateRight(Node node){
        Node result = node.left;
        node.left = result.right;
        result.right = node;
        return result;
    }

    private Node rotateLeft(Node node){
        Node result = node.right;
        node.right = result.left;
        result.left = node;
        return result;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Nullable private Node find(@Nullable Node node, @NotNull K key){
        if(node == null) return null;
        int comp = keyComp.compare(key, node.key);
        if(comp > 0) return find(node.right, key);
        else if(comp < 0) return find(node.left, key);
        else return node;
    }

    @Override
    public Iterator<K> keys() {
        Stack<Node> initialStack = new Stack<>();
        if(head != null) initialStack.push(head);

        return new Iterator<K>() {
            private Stack<Node> stack = initialStack;
            private Stack<Node> visited = new Stack<>();

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public K next() {
                while(true) {
                    Node current = stack.pop();
                    if (visited.isEmpty() || visited.peek() != current) {
                        if (current.right != null) stack.push(current.right);
                        stack.push(current);
                        visited.push(current);
                        if (current.left != null) stack.push(current.left);
                    } else {
                        visited.pop();
                        return current.key;
                    }
                }
            }
        };
    }

    protected class Node {
        K key;
        V value;
        Node left;
        Node right;

        Node(K key, V value) {
            this.value = value;
            this.key = key;
        }
    }

    protected class NodeValuePair{
        Node node;
        V value;

        NodeValuePair(Node node, V value) {
            this.node = node;
            this.value = value;
        }
    }
}


