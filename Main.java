import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> collected_words = new HashMap<>();
        ArrayList<String> words_in_file;

        String[] file_names = {"Kafka.txt", "Kronin.txt", "Tolstoy.txt"};

        for (String fileName : file_names) {
            words_in_file = ReadFile.read_file(fileName); 

            for (String word : words_in_file) {
                collected_words.put(word, collected_words.getOrDefault(word, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(collected_words.entrySet());
        sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        ArrayList<String> sortedWords = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            sortedWords.add(entry.getKey() + ": " + entry.getValue());
        }

        WriteToFile.writeWordsToFile(sortedWords, "dictionary.txt");
    }
}

class ReadFile {
    public static ArrayList<String> read_file(String file_name) {
    ArrayList<String> words_in_file = new ArrayList<String>();
    String regex = "[\\u0401\\u0451\\u0410-\\u044f\\w]+";
    Pattern p = Pattern.compile(regex);
        try {
            File file = new File(file_name);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                words_in_file.addAll(p.matcher(line).results().map(MatchResult::group).collect(Collectors.toList()));
            }
            reader.close();
        } catch (Exception error) {
            System.out.println("Error reading file: " + error.getMessage());
        }
        return words_in_file;
    }
}

class WriteToFile {
    public static void writeWordsToFile(ArrayList<String> words, String outputFileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String word : words) {
                writer.write(word);
                writer.newLine(); // Add a new line after each word
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}