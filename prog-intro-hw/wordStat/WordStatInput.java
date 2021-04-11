package wordStat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordStatInput {
    private final static String pattern = "[^\\p{L}\\p{Pd}']";

    public static void main(String[] args) {
        Map<String, Integer> cnt = new HashMap<>();
        List<String> words = new ArrayList<>();
        try {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(args[0]),
                            StandardCharsets.UTF_8
                    )
            ); BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(args[1]),
                            StandardCharsets.UTF_8
                    )
            )) {
                String str = reader.readLine();
                while (str != null) {
                    for (String word : str.split(pattern)) {
                        if (!word.isEmpty()) {
                            word = word.toLowerCase();
                            if (!cnt.containsKey(word)) {
                                cnt.put(word, 1);
                                words.add(word);
                            } else {
                                cnt.put(word, cnt.get(word) + 1);
                            }
                        }
                    }
                    str = reader.readLine();
                }
                for (String word : words) {
                    writer.write(word + " " + cnt.get(word) + "\n");
                }

            }
        } catch (IndexOutOfBoundsException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
