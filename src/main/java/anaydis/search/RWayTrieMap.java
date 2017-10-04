package anaydis.search;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Tomas Perez Molina
 */
public class RWayTrieMap<V> implements Map<String,V>{
    private int size;
    private Node<V> head;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(@NotNull String key) {
        Node<V> node = find(head, key, 0);
        return node != null && node.value != null;
    }

    @Override
    public V get(@NotNull String key) {
        final Node<V> node = find(head, key, 0);
        if(node != null) return node.value;
        else return null;
    }

    NodeValuePair put(Node<V> node, @NotNull String key, V value, int level){
        V prevValue = null;
        Node<V> resultNode;
        if(node == null){
            resultNode = new Node<>(null);
            if(level < key.length()){
                int next = key.charAt(level);
                final NodeValuePair putPair = put(resultNode.next[next], key, value, level + 1);
                resultNode.next[next] =  putPair.node;
                prevValue = putPair.value;
            } else {
                size++;
                resultNode.value = value;
            }
        }
        else if(level == key.length()){
            prevValue = node.value;
            if(prevValue == null) size++;
            node.value = value;
            resultNode = node;
        }
        else {
            int next = key.charAt(level);
            final NodeValuePair putPair = put(node.next[next], key, value, level + 1);
            node.next[next] = putPair.node;
            prevValue = putPair.value;
            resultNode = node;
        }
        return new NodeValuePair(resultNode, prevValue);
    }

    @Override
    public V put(@NotNull String key, V value) {
        NodeValuePair putPair = put(head, key, value, 0);
        head = putPair.node;
        return putPair.value;
    }

    @Nullable Node<V> find(@Nullable Node<V> node, @NotNull String key, int level){
        if(node == null) return null;
        if(level == key.length()) return node;
        int next = key.charAt(level);
        return find(node.next[next], key, level+1);
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public Iterator<String> keys() {
        List<String> keys = new ArrayList<>();
        if(head != null) traverse(head, "", keys);
        return keys.iterator();
    }

    void traverse(Node<V> node, String originKey, List<String> target){
        Node<V>[] array = node.next;
        for (int i = 0; i < array.length; i++) {
            Node<V> currentNode = array[i];
            if(array[i] != null){
                String nodeKey = originKey + (char) i;
                if(currentNode.value != null) target.add(nodeKey);
                traverse(currentNode, nodeKey, target);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public class Node<V>{
        V value;
        Node<V>[] next = new Node[256];


        public Node(V value) {
            this.value = value;
        }
    }

    protected class NodeValuePair{
        Node<V> node;
        V value;

        NodeValuePair(Node node, V value) {
            this.node = node;
            this.value = value;
        }
    }
}
