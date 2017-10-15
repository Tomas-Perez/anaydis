package anaydis.compression;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Tomas Perez Molina
 */
public class HuffmanTable {
    private HashMap<Character, HuffmanKey> table;
    private List<CharacterFreqPair> characterFreqPairs;

    public HuffmanTable(List<CharacterFreqPair> pairs){
        characterFreqPairs = pairs;
        table = new HashMap<>(pairs.size());
        HuffmanNode head = createTree(pairs);
        fillTable(head, (byte) 0, 0);
    }

    public MsgLengthAndSigBits getMessageSizeInBytes(){
        int msgSize = characterFreqPairs.stream()
                .mapToInt(
                    pair -> {
                        int keySize = table.get(pair.value).size;
                        return pair.freq * (keySize > 0 ? keySize : 1);
                    })
                .sum();
        return new MsgLengthAndSigBits((msgSize + 7) / 8, msgSize % 8); // round up and get bytes
    }

    public HuffmanKey getKey(Character character){
        return table.get(character);
    }

    public List<Pair<Character, HuffmanKey>> getKeyValuePairs(){
        List<Pair<Character, HuffmanKey>> pairs = new ArrayList<>(table.size());
        table.forEach((key, value) -> pairs.add(new Pair<>(key, value)));
        return pairs;
    }

    public byte[] toByteArray() {
        byte[] array = new byte[table.size() * 3];
        int i = 0;
        for (Character character : table.keySet()) {
            array[i] = table.get(character).key;
            array[i+1] = table.get(character).size;
            array[i+2] = (byte) character.charValue();
            i += 3;
        }
        return array;
    }

    void fillTable(HuffmanNode node, Byte key, int level){
        if(node.isLeaf()){
            table.put(node.value, new HuffmanKey(key, (byte) (level > 0? level : 1)));
        }
        else {
            if(node.left != null) fillTable(node.left, turnOffAndShift(key), level + 1);
            if(node.right != null) fillTable(node.right, turnOnAndShift(key), level + 1);
        }
    }

    Byte turnOnAndShift(byte num){
        return (byte) ((num << 1| 1) );
    }

    Byte turnOffAndShift(byte num){
        return (byte) (num << 1 & ~(1));
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

    public static class HuffmanKey{
        byte key;
        byte size;

        public HuffmanKey(byte key, byte size) {
            this.key = key;
            this.size = size;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HuffmanKey)) return false;

            HuffmanKey that = (HuffmanKey) o;

            if (key != that.key) return false;
            return size == that.size;
        }

        @Override
        public int hashCode() {
            int result = (int) key;
            result = 31 * result + (int) size;
            return result;
        }
    }

    public static class MsgLengthAndSigBits{
        int msgLength;
        int sigBits;

        public MsgLengthAndSigBits(int msgLength, int sigBits) {
            this.msgLength = msgLength;
            this.sigBits = sigBits;
        }
    }
}
