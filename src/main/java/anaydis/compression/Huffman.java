package anaydis.compression;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
        output.write(table.toByteArray());
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
            HuffmanTable.HuffmanKey key = table.getKey((char) read_byte);
            for (int j = key.size - 1; j >= 0; j--) {
                if (bitAt(key.key, j)) acum = turnOnAndShift(acum);
                else acum = turnOffAndShift(acum);
                sigBits++;
                if (sigBits == BYTE_SIZE) {
                    output.write(acum);
                    acum = 0;
                    sigBits = 0;
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
        while(read_byte != CONTROL_VAL){
            int key = read_byte;
            int size = input.read();
            char character = (char) input.read();
            keyMap.put(new HuffmanTable.HuffmanKey((byte) key, (byte) size), character);
            read_byte = input.read();
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
        byte acum = 0, size = 0;
        int byteCount = 0, lowerLimit = 0;
        while(byteCount < msgSize){
            if(byteCount == msgSize - 1 && lastSigBit != 0) lowerLimit = BYTE_SIZE - lastSigBit;
            for(int i = BYTE_SIZE - 1; i >= lowerLimit; i--){
                if(bitAt(read_byte, i)){
                    acum = turnOnAndShift(acum);
                }
                else {
                    acum = turnOffAndShift(acum);
                }
                size++;
                final Character character = keyMap.get(new HuffmanTable.HuffmanKey(acum, size));
                if(character != null){
                    output.write(character);
                    acum = 0;
                    size = 0;
                }
            }
            read_byte = input.read();
            byteCount++;
        }
        input.close();
        output.close();
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

    boolean bitAt(int num, int at){
        return (num >> at & 1) != 0;
    }

    Byte turnOnAndShift(byte num){
        return (byte) ((num << 1 | 1));
    }

    Byte turnOffAndShift(byte num){
        return (byte) (num << 1 & ~(1));
    }

    Byte copy(byte num, int toIndex){
        byte mask = (byte) (0b11111111 >>> (BYTE_SIZE - 1 - toIndex));
        return (byte) (num & mask);
    }
}
