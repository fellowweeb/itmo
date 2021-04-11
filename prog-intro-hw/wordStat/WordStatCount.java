package wordStat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatCount {
    private static final String pattern = "[^\\p{L}\\p{Pd}']";

    public static void main(String[] args) {
        Map<String, Integer> wordsCount = new LinkedHashMap<>();
        try {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);
            try (Reader reader = new BufferedReader(new FileReader(inputFile, StandardCharsets.UTF_8))) {
                String str = readLine(reader);
                while (str != null) {
                    for (String word : str.toLowerCase().split(pattern)) {
                        if (!word.isEmpty()) {
                            wordsCount.put(word, wordsCount.getOrDefault(word, 0) + 1);
                        }
                    }
                    str = readLine(reader);
                }
            }
            try (Writer writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {
                List<Map.Entry<String, Integer>> cntList = new ArrayList<>(wordsCount.entrySet());
                Comparator<Map.Entry<String, Integer>> comparator = Comparator.comparing(Map.Entry::getValue);
                cntList.sort(comparator);
                for (Map.Entry<String, Integer> el : cntList) {
                    writer.write(el.getKey() + " " + el.getValue() + "\n");
                }
            }
        } catch (IndexOutOfBoundsException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String readLine(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int ch = reader.read();
        if (ch == -1)
            return null;
        while (ch != -1 && ch != '\n') {
            builder.append((char) ch);
            ch = reader.read();
        }
        return builder.toString();
    }
}
