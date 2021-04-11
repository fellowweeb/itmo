package wordStat;

import scanner.Scanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WordStatLastIndex {
    private static final String pattern = "[^\\p{L}\\p{Pd}']";

    public static void main(String[] args) {
        Map<String, IntList> wordsIndex = new LinkedHashMap<>();
        Map<String, Integer> wordsCount = new HashMap<>();
        try {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);
            try (Scanner scanner = new Scanner(inputFile, "utf8")) {
                String str = scanner.nextLine();
                while (str != null) {
                    Map<String, Integer> wordsLineIndex = new LinkedHashMap<>();
                    int index = 1;
                    for (String word : str.toLowerCase().split(pattern)) {
                        if (!word.isEmpty()) {
                            wordsLineIndex.put(word, index);
                            wordsCount.put(word, wordsCount.getOrDefault(word, 0) + 1);
                            index++;
                        }
                    }
                    for (Map.Entry<String, Integer> el : wordsLineIndex.entrySet()) {
                        if (wordsIndex.containsKey(el.getKey())) {
                            wordsIndex.get(el.getKey()).pushBack(el.getValue());
                        } else {
                            wordsIndex.put(el.getKey(), new IntList(1, el.getValue()));
                        }
                    }
                    str = scanner.nextLine();
                }
            }
            try (Writer writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {
                for (Map.Entry<String, IntList> el : wordsIndex.entrySet()) {
                    writer.write(el.getKey() + " " + wordsCount.get(el.getKey()) + " ");
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
