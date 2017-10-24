package anaydis.compression;

import org.jetbrains.annotations.NotNull;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * @author Tomas Perez Molina
 */
class HuffmanTable {
    private HashMap<Character, HuffmanKey> table;
    private List<CharacterFreqPair> characterFreqPairs;

    HuffmanTable(List<CharacterFreqPair> pairs){
        characterFreqPairs = pairs;
        table = new HashMap<>(pairs.size());
        HuffmanNode head = createTree(pairs);
        fillTable(head, new byte[]{0}, 0);
    }

    MsgLengthAndSigBits getMessageSizeInBytes(){
        int msgSize = characterFreqPairs.stream()
                .mapToInt(
                    pair -> {
                        int keySize = table.get(pair.value).size;
                        return pair.freq * (keySize > 0 ? keySize : 1);
                    })
                .sum();
        return new MsgLengthAndSigBits((msgSize + 7) / 8, msgSize % 8); // round up and get bytes
    }

    HuffmanKey getKey(Character character){
        return table.get(character);
    }

    byte[] toByteArray() {
        ArrayList<Byte> list = new ArrayList<>(table.size() * 3);
        for (Character character : table.keySet()) {
            for (byte pos : longToByteArray((long) table.get(character).size)) {
                list.add(pos);
            }
            final byte CONTROL_VAL = (byte) 255;
            list.add(CONTROL_VAL);
            for (byte pos : table.get(character).key) {
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

    private byte[] longToByteArray(Long aLong){
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

    private void fillTable(HuffmanNode node, byte[] key, int level){
        if(node.isLeaf()){
            table.put(node.value, new HuffmanKey(key, (level > 0? level : 1)));
        }
        else {
            byte[] newKey = key;
            final int BYTE_SIZE = 8;
            if(level > 1 && level % BYTE_SIZE == 0){
                newKey = new byte[key.length + 1];
                System.arraycopy(key, 0, newKey, 0, key.length);
            }
            if(node.left != null){
                byte[] leftKey = Arrays.copyOf(newKey, newKey.length);
                leftKey[leftKey.length - 1] = turnOffAndShift(leftKey[leftKey.length - 1]);
                fillTable(node.left, leftKey, level + 1);
            }
            if(node.right != null){
                byte[] rightKey = Arrays.copyOf(newKey, newKey.length);
                rightKey[rightKey.length - 1] = turnOnAndShift(rightKey[rightKey.length - 1]);
                fillTable(node.right, rightKey, level + 1);
            }
        }
    }

    private byte turnOnAndShift(byte num){
        return (byte) (num << 1 | 1);
    }

    private byte turnOffAndShift(byte num){
        return (byte) (num << 1 & ~(1));
    }

    private HuffmanNode createTree(List<CharacterFreqPair> pairs){
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

    static class CharacterFreqPair{
        int freq;
        char value;

        CharacterFreqPair(int freq, char value) {
            this.freq = freq;
            this.value = value;
        }
    }

    public static class HuffmanKey{
        byte[] key;
        int size;

        HuffmanKey(byte[] key, int size) {
            this.key = key;
            this.size = size;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HuffmanKey)) return false;

            HuffmanKey that = (HuffmanKey) o;

            return size == that.size && Arrays.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(key);
            result = 31 * result + size;
            return result;
        }
    }

    static class MsgLengthAndSigBits{
        int msgLength;
        int sigBits;

        MsgLengthAndSigBits(int msgLength, int sigBits) {
            this.msgLength = msgLength;
            this.sigBits = sigBits;
        }
    }
}
