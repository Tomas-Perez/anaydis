package anaydis.practice.compressionSandbox;

import anaydis.sort.IntegerDataSetGenerator;
import anaydis.sort.data.DataSetGenerator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.PriorityQueue;

/**
 * @author Tomas Perez Molina
 */
public class sandbox {
    public static void main(String[] args) throws Exception{
//        String test = "This is a test ñ";
//        InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8.name()));
//        int Byte = stream.read();
//        while(Byte != -1){
//            System.out.println(Byte + ", " + (char) Byte);
//            Byte = stream.read();
//        }
//        int binary = 0b00000001;
//        int binary2 = 0b01011000000000000000000000000000;
//        System.out.println(binary);
//        System.out.println(binary2);
//        System.out.println((char) 250);
        DataSetGenerator<Integer> generator = new IntegerDataSetGenerator();
        PriorityQueue<Integer> queue = new PriorityQueue<>(generator.createRandom(10));
        while(!queue.isEmpty()){
            System.out.println(queue.poll());
        }
//        for(int i = 0; i < 1024; i++){
//            System.out.println((char) i);
//        }
    }
}
