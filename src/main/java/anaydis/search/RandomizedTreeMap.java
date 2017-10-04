package anaydis.search;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Tomas Perez Molina
 */
public class RandomizedTreeMap<K, V> implements Map<K,V> {
    private Node<K, V> head;
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
        final Node<K, V> node = find(head, key);
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

    NodeValuePair put(@Nullable Node<K,V> node, K key, V value){
        V prevValue = null;
        Node<K, V> resultNode;
        if(node == null){
            size++;
            resultNode = new Node<>(key, value);
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

    NodeValuePair rootPut(@Nullable Node<K,V> node, K key, V value){
        V prevValue = null;
        Node<K, V> resultNode;
        if(node == null){
            size++;
            resultNode = new Node<>(key, value);
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

    private Node<K, V> rotateRight(Node<K, V> node){
        Node<K, V> result = node.left;
        node.left = result.right;
        result.right = node;
        return result;
    }

    private Node<K, V> rotateLeft(Node<K, V> node){
        Node<K, V> result = node.right;
        node.right = result.left;
        result.left = node;
        return result;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Nullable Node<K, V> find(@Nullable Node<K, V> node, @NotNull K key){
        if(node == null) return null;
        int comp = keyComp.compare(key, node.key);
        if(comp > 0) return find(node.right, key);
        else if(comp < 0) return find(node.left, key);
        else return node;
    }

    @Override
    public Iterator<K> keys() {
        List<K> keys = new ArrayList<>(size);
        inOrder(head, keys);
        return keys.iterator();
    }

    private void inOrder(Node<K, V> node, List<K> target) {
        if(node == null) return;
        inOrder(node.left, target);
        target.add(node.key);
        inOrder(node.right, target);
    }

    protected class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value) {
            this.value = value;
            this.key = key;
        }
    }

    protected class NodeValuePair{
        Node<K, V> node;
        V value;

        NodeValuePair(Node<K, V> node, V value) {
            this.node = node;
            this.value = value;
        }
    }
}


