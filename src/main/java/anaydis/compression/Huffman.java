package anaydis.compression;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */

public class Huffman implements Compressor {
    private final int CONTROL_VAL = 255;
    private final int BYTE_SIZE = 8;
    private final int MAX_BYTE_POSITIVE = 127;

    @Override
    public void encode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        input.mark(Integer.MAX_VALUE);
        HuffmanTable table = new HuffmanTable(countCharacters(input));
        HuffmanTable.MsgLengthAndSigBits msgLengthAndSigBits = table.getMessageSizeInBytes();
        int msgLength = msgLengthAndSigBits.msgLength;
        int lastSigBits = msgLengthAndSigBits.sigBits;
        input.reset();
        final byte[] tableArray = table.toByteArray();
        output.write(tableArray);
        output.write(CONTROL_VAL);
        output.write(CONTROL_VAL);
        while(msgLength > MAX_BYTE_POSITIVE){
            output.write(MAX_BYTE_POSITIVE);
            msgLength -= MAX_BYTE_POSITIVE;
        }
        output.write(msgLength);
        output.write(CONTROL_VAL);
        output.write(lastSigBits);
        output.write(CONTROL_VAL);
        int read_byte = input.read();
        byte acum = 0;
        int sigBits = 0;
        while (read_byte != -1) {
            HuffmanTable.HuffmanKey huffmanKey = table.getKey((char) read_byte);
            byte[] keyArray = huffmanKey.key;
            for (int i = 0; i < keyArray.length; i++) {
                int initialIndex = 0;
                if(i == 0) initialIndex = huffmanKey.size - 1;
                for (int j = initialIndex; j >= 0; j--) {
                    if (bitAt(keyArray[i], j)) acum = turnOnAndShift(acum);
                    else acum = turnOffAndShift(acum);
                    sigBits++;
                    if (sigBits == BYTE_SIZE) {
                        output.write(acum);
                        acum = 0;
                        sigBits = 0;
                    }
                }
            }
            read_byte = input.read();
        }
        if(sigBits > 0){
            output.write(acum << BYTE_SIZE - sigBits);
        }
        input.close();
        output.close();
    }

    @Override
    public void decode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        HashMap<HuffmanTable.HuffmanKey, Character> keyMap = new HashMap<>();
        int read_byte = input.read();
        while(true){
            byte[] sizeArray = new byte[4];
            int j = 1;
            while (read_byte != CONTROL_VAL) {
                sizeArray[sizeArray.length - j] = (byte) read_byte;
                j++;
                read_byte = input.read();
            }
            int bitSize = ByteBuffer.wrap(sizeArray).getInt();
            int byteSize = (bitSize + 7) / 8;
            byte[] key = new byte[byteSize];
            for (int i = 0; i < key.length; i++) {
                key[i] = (byte) input.read();
            }
            char character = (char) input.read();
            keyMap.put(new HuffmanTable.HuffmanKey(key, bitSize), character);
            read_byte = input.read();
            if(read_byte == CONTROL_VAL){
                input.mark(2);
                int next_byte = input.read();
                if(next_byte == CONTROL_VAL) break;
                else{
                    read_byte = next_byte;
                }
            }
        }
        read_byte = input.read();
        int msgSize = 0;
        while(read_byte != CONTROL_VAL){
            msgSize += read_byte;
            read_byte = input.read();
        }
        int lastSigBit = input.read();
        input.read();
        read_byte = input.read();
        byte[] acum = new byte[]{0};
        int readSize = 0;
        int byteCount = 0, lowerLimit = 0;
        while(byteCount < msgSize){
            if(byteCount == msgSize - 1 && lastSigBit != 0) lowerLimit = BYTE_SIZE - lastSigBit;
            for(int i = BYTE_SIZE - 1; i >= lowerLimit; i--){
                if(bitAt(read_byte, i)){
                    acum[0] = turnOnAndShift(acum[0]);
                }
                else {
                    acum[0] = turnOffAndShift(acum[0]);
                }
                readSize++;
                final Character character = keyMap.get(new HuffmanTable.HuffmanKey(acum, readSize));
                if(character != null){
                    output.write(character);
                    acum = new byte[]{0};
                    readSize = 0;
                }
                else if(readSize > 1 && (readSize) % BYTE_SIZE == 0){
                    byte[] newAcum = new byte[acum.length + 1];
                    for (int k = 1; k <= acum.length; k++) {
                        newAcum[newAcum.length - k] = acum[acum.length - k];
                    }
                    acum = newAcum;
                }
            }
            read_byte = input.read();
            byteCount++;
        }
        input.close();
        output.close();
    }

    private long extractLong(@NotNull InputStream input, int size) throws IOException {
        byte[] keyArray = new byte[8];
        int initialIndex = keyArray.length - 1 - ((size - 1) / 8);
        for (int i = initialIndex; i < keyArray.length; i++) {
            keyArray[i] = (byte) input.read();
        }
        return ByteBuffer.wrap(keyArray).getLong();
    }

    List<HuffmanTable.CharacterFreqPair> countCharacters(@NotNull InputStream input) throws IOException{
        HashMap<Character, Integer> frequencies = new HashMap<>();
        int readByte = input.read();
        while(readByte != -1){
            frequencies.merge((char) readByte, 1, (a, b) -> a + b);
            readByte = input.read();
        }
        List<HuffmanTable.CharacterFreqPair> result = new ArrayList<>();
        frequencies.forEach((k, v) -> result.add(new HuffmanTable.CharacterFreqPair(v, k)));
        return result;
    }

    boolean bitAt(long num, int at){
        return (num >> at & 1) != 0;
    }

    byte turnOnAndShift(byte num){
        return (byte) (num << 1 | 1);
    }

    byte turnOffAndShift(byte num){
        return (byte) (num << 1 & ~(1));
    }
}
