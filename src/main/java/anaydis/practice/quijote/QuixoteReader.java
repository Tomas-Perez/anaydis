package anaydis.practice.quijote;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Tomas Perez Molina
 */
public class QuixoteReader {
    public static void main(String[] args) {
        quijoteWords();
    }

    private static Stream<String> quijoteWords(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("./src/test/resources/books/quijote_parsed.txt"));
            return br.lines().flatMap(line -> Arrays.stream(line.split(",")));
        } catch (FileNotFoundException exc){
            System.out.println(exc);
            System.exit(1);
        }
        return null;
    }
}
