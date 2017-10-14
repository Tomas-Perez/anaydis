package anaydis.compression;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author Tomas Perez Molina
 */
public class HuffmanTable {
    private HashMap<Integer, Character> table;

    public HuffmanTable(List<CharacterFreqPair> pairs){
        table = new HashMap<>(pairs.size());
        HuffmanNode head = createTree(pairs);
        fillTable(head, 0, 0);
    }

    public Character get(int key){
        return table.get(key);
    }

    public List<Pair<Integer, Character>> getKeyValuePairs(){
        List<Pair<Integer, Character>> pairs = new ArrayList<>(table.size());
        table.forEach((key, value) -> pairs.add(new Pair<>(key, value)));
        return pairs;
    }

    void fillTable(HuffmanNode node, int key, int level){
        if(node.isLeaf()){
            table.put(key, node.value);
        }
        else {
            if(node.left != null) fillTable(node.left, turnOffBitAt(key, level), level + 1);
            if(node.right != null) fillTable(node.right, turnOnBitAt(key, level), level + 1);
        }
    }

    int turnOnBitAt(int num, int at){
        return num | (1 << at);
    }

    int turnOffBitAt(int num, int at){
        return num & ~(1 << at);
    }

    HuffmanNode createTree(List<CharacterFreqPair> pairs){
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(pairs.size());
        pairs.stream()
                .map(pair -> new HuffmanNode(pair.freq, pair.value))
                .forEach(priorityQueue::add);
        while(priorityQueue.size() > 1){
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            priorityQueue.add(connect(left, right));
        }
        return priorityQueue.poll();
    }

    private HuffmanNode connect(HuffmanNode left, HuffmanNode right){
        return new HuffmanNode(left.freq + right.freq, left, right);
    }

    private class HuffmanNode implements Comparable<HuffmanNode>{
        int freq;
        char value;
        HuffmanNode left;
        HuffmanNode right;

        HuffmanNode(int freq, char value) {
            this.freq = freq;
            this.value = value;
        }

        HuffmanNode(int freq, HuffmanNode left, HuffmanNode right) {
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(@NotNull HuffmanNode o) {
            return this.freq - o.freq;
        }

        boolean isLeaf(){
            return this.left == null && this.right == null;
        }
    }

    public static class CharacterFreqPair{
        int freq;
        char value;

        public CharacterFreqPair(int freq, char value) {
            this.freq = freq;
            this.value = value;
        }
    }
}
