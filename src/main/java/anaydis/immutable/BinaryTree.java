package anaydis.immutable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


/**
 * @author Tomas Perez Molina
 */
public class BinaryTree<K, V> implements Map<K, V>{
    private Node head;
    private final int size;
    private final Comparator<K> keyComp;

    public BinaryTree(Comparator<K> keyComp) {
        this.keyComp = keyComp;
        this.size = 0;
    }

    private BinaryTree(Comparator<K> keyComp, Node head, int size) {
        this.head = head;
        this.size = size;
        this.keyComp = keyComp;
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
    public Map<K, V> put(@NotNull K key, V value) {
        NodeIncrementPair nodeIncrementPair = put(head, key, value);
        if(nodeIncrementPair.increment){
            return new BinaryTree<>(keyComp, nodeIncrementPair.node, size + 1);
        }
        return new BinaryTree<>(keyComp, nodeIncrementPair.node, size);
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        final Node node = find(head, key);
        if(node != null) return node.value;
        else return null;
    }


    NodeIncrementPair put(@Nullable Node node, K key, V value){
        Node resultNode;
        NodeIncrementPair nodeIncrementPair;
        NodeIncrementPair resultPair;
        if(node == null){
            resultNode = new Node(key, value);
            resultPair = new NodeIncrementPair(resultNode, true);
        }
        else {
            int comp = keyComp.compare(key, node.key);
            if(comp > 0){
                resultNode = new Node(node.key, node.value);
                resultNode.left = node.left;
                nodeIncrementPair = put(node.right, key, value);
                resultNode.right = nodeIncrementPair.node;
                resultPair = new NodeIncrementPair(resultNode, nodeIncrementPair.increment);
            }
            else if(comp < 0){
                resultNode = new Node(node.key, node.value);
                resultNode.right = node.right;
                nodeIncrementPair = put(node.left, key, value);
                resultNode.left = nodeIncrementPair.node;
                resultPair = new NodeIncrementPair(resultNode, nodeIncrementPair.increment);
            }
            else {
                resultNode = new Node(node.key, value);
                resultNode.right = node.right;
                resultNode.left = node.left;
                resultPair = new NodeIncrementPair(resultNode, false);
            }
        }
        return resultPair;
    }

    @Nullable Node find(@Nullable Node node, @NotNull K key){
        if(node == null) return null;
        int comp = keyComp.compare(key, node.key);
        if(comp > 0) return find(node.right, key);
        else if(comp < 0) return find(node.left, key);
        else return node;
    }

    @Override
    public Iterator<K> keys() {
        java.util.List<K> keys = new ArrayList<>(size);
        inOrder(head, keys);
        return keys.iterator();
    }

    private void inOrder(Node node, java.util.List<K> target) {
        if(node == null) return;
        inOrder(node.left, target);
        target.add(node.key);
        inOrder(node.right, target);
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

    protected class NodeIncrementPair {
        Node node;
        boolean increment;

        public NodeIncrementPair(Node node, boolean increment) {
            this.node = node;
            this.increment = increment;
        }
    }
}
