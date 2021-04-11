package wordStat;

import scanner.Scanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class WordStatIndex {
    private static final String pattern = "[^\\p{L}\\p{Pd}']";

    public static void main(String[] args) {
        Map<String, IntList> wordsIndex = new LinkedHashMap<>();
        try {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);
            try (Scanner scanner = new Scanner(inputFile, "utf8")) {
                String str = scanner.nextLine();
                int index = 1;
                while (str != null) {
                    for (String word : str.toLowerCase().split(pattern)) {
                        if (!word.isEmpty()) {
                            if (wordsIndex.containsKey(word)) {
                                wordsIndex.get(word).pushBack(index);
                            } else {
                                wordsIndex.put(word, new IntList(1, index));
                            }
                            index++;
                        }
                    }
                    str = scanner.nextLine();
                }
            }
            try (Writer writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {
                for (Map.Entry<String, IntList> el : wordsIndex.entrySet()) {
                    writer.write(el.getKey() + " " + el.getValue().size() + " ");
                    for (int i = 0; i < el.getValue().size() - 1; i++) {
                        writer.write(el.getValue().get(i) + " ");
                    }
                    writer.write(el.getValue().back() + System.lineSeparator());
                }
            }
        } catch (IndexOutOfBoundsException | IOException e) {
            e.printStackTrace();
        }
    }
}
