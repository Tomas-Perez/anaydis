package anaydis.search;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author Tomas Perez Molina
 */
public class TSTTrieMap<V> implements Map<String, V>{
    private int size;
    private TripleNode head;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(@NotNull String key) {
        final TripleNode findNode = find(head, key, 0);
        return findNode != null && findNode.value != null;
    }

    @Override
    public V get(@NotNull String key) {
        final TripleNode findNode = find(head, key, 0);
        return findNode == null? null : findNode.value;
    }

    @Override
    public V put(@NotNull String key, V value) {
        final NodeValuePair put = put(head, key, value, 0);
        head = put.node;
        return put.value;
    }

    private NodeValuePair put(TripleNode node, @NotNull String key, V value, int level){
        TripleNode result;
        V oldVal = null;
        if(node == null){
            result = new TripleNode(key.charAt(level));
            if(level < key.length() - 1){
                result.middle = put(result.middle, key, value, level + 1).node;
            }
            else{
                result.value = value;
                size++;
            }
        }
        else{
            if(node.key == key.charAt(level)){
                if(level == key.length() - 1){
                    oldVal = node.value;
                    if(oldVal == null) size++;
                    node.value = value;
                }
                else{
                    final NodeValuePair put = put(node.middle, key, value, level + 1);
                    node.middle = put.node;
                    oldVal = put.value;
                }
            }
            else if(key.charAt(level) < node.key){
                final NodeValuePair put = put(node.left, key, value, level);
                node.left = put.node;
                oldVal = put.value;
            }
            else{
                final NodeValuePair put = put(node.right, key, value, level);
                node.right = put.node;
                oldVal = put.value;
            }
            result = node;
        }
        return new NodeValuePair(result, oldVal);
    }

    private TripleNode find(TripleNode node, String key, int level){
        if(node == null) return null;
        if(key.charAt(level) == node.key){
            if(level == key.length() - 1) return node;
            else return find(node.middle, key, level + 1);
        }
        else if(key.charAt(level) < node.key){
            return find(node.left, key, level);
        }
        else{
            return find(node.right, key, level);
        }
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public Iterator<String> keys() {
        Stack<NodeFullKeyPair> initialStack = new Stack<>();
        initialStack.push(new NodeFullKeyPair(head, ""));

        return new Iterator<String>() {
            private Stack<NodeFullKeyPair> stack = initialStack;

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public String next() {
                TripleNode node;
                String key;

                do {
                    NodeFullKeyPair current = stack.pop();
                    node = current.node;
                    key = current.key;

                    if(node.right != null) stack.push(new NodeFullKeyPair(node.right, key));
                    if(node.middle != null) stack.push(new NodeFullKeyPair(node.middle, key + node.key));
                    if(node.left != null) stack.push(new NodeFullKeyPair(node.left, key));
                } while(node.value == null);
                return key + node.key;
            }
        };
    }

    private class TripleNode{
        char key;
        V value;
        TripleNode left;
        TripleNode right;
        TripleNode middle;

        TripleNode(char key) {
            this.key = key;
        }
    }

    private class NodeValuePair{
        TripleNode node;
        V value;

        NodeValuePair(TripleNode node, V value) {
            this.node = node;
            this.value = value;
        }
    }

    private class NodeFullKeyPair{
        TripleNode node;
        String key;

        NodeFullKeyPair(TripleNode node, String key) {
            this.node = node;
            this.key = key;
        }
    }
}
