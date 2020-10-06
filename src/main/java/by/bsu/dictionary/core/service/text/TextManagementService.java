package by.bsu.dictionary.core.service.text;

import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.service.word.WordManagementService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TextManagementService {

    private final WordManagementService wordManagementService;

    public void deleteWordFromText(String oldWord) {
        String text = String.valueOf(uploadText());
        if (text.contains(oldWord)) {
            String tempWord = oldWord + " ";
            text = text.replaceAll(tempWord, " ");

            tempWord = " " + oldWord;
            text = text.replaceAll(tempWord, " ");
        }
        saveTextToFile(text);
    }

    public void replaceWordFromTextWithNewOne(String oldWord, String newWord) {
        String text = String.valueOf(uploadText());
        if (text.contains(oldWord)) {
            String tempWord = oldWord + " ";
            text = text.replaceAll(tempWord, " " + newWord + " ");

            tempWord = " " + oldWord;
            text = text.replaceAll(tempWord, " " + newWord + " ");
        }
        saveTextToFile(text);
        saveEditedWord(oldWord, newWord);
    }

    public void addNewWord(Word word) {
        wordManagementService.save(word);
        StringBuilder stringBuilder = uploadText();
        stringBuilder.append(" ").append(word.getName());
        saveTextToFile(String.valueOf(stringBuilder));
    }

    public StringBuilder uploadText() {
        File file = new File("/home/luba/Projects/dictionary/text.txt");
        String st;
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((st = br.readLine()) != null) {
                stringBuilder.append(st);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder;
    }

    public void parseText() {
        String[] words = String.valueOf(uploadText()).split("\\W+");
        Map<String, Long> nameFrequency = Arrays.stream(words)
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        wordManagementService.save(nameFrequency);
    }

    public void saveEditedWord(String oldWord, String newWord) {
        wordManagementService.saveEditedWord(oldWord, newWord);
    }

    @SneakyThrows
    private void saveTextToFile(String text) {
        FileWriter fileWriter = new FileWriter("text.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(text);
        printWriter.close();
        fileWriter.close();
    }

}
