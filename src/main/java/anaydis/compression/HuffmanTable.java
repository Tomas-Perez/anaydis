package anaydis.compression;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
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
        fillTable(head, 0L, 0);
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
        ArrayList<Byte> list = new ArrayList<>(table.size() * 3);
        for (Character character : table.keySet()) {
            for (byte pos : longToByteArray((long) table.get(character).size)) {
                list.add(pos);
            }
            for (byte pos : longToByteArray(table.get(character).key)) {
                list.add(pos);
            }
            list.add((byte) character.charValue());
        }
        byte[] result = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    byte[] longToByteArray(Long aLong){
        byte[] array = new byte[8];
        ByteBuffer.wrap(array).putLong(aLong);
        int actualLength = 0;
        for (int i = 0; i < array.length; i++) {
            if(array[i] != 0) {
                actualLength = array.length - i;
                break;
            }
        }
        byte[] result;
        if(actualLength == 0){
            result = new byte[]{0};
        }
        else if(actualLength == array.length){
            result = array;
        }
        else {
            result = new byte[actualLength];
            for (int i = 1; i <= actualLength; i++) {
                result[actualLength - i] = array[array.length - i];
            }
        }
        return result;
    }

    void fillTable(HuffmanNode node, Long key, int level){
        if(node.isLeaf()){
            table.put(node.value, new HuffmanKey(key, (level > 0? level : 1)));
        }
        else {
            if(node.left != null) fillTable(node.left, turnOffAndShift(key), level + 1);
            if(node.right != null) fillTable(node.right, turnOnAndShift(key), level + 1);
        }
    }

    Long turnOnAndShift(long num){
        return (num << 1L | 1L);
    }

    Long turnOffAndShift(long num){
        return num << 1L & ~(1L);
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
        long key;
        int size;

        public HuffmanKey(long key, int size) {
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
            int result = (int) (key ^ (key >>> 32));
            result = 31 * result + size;
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
