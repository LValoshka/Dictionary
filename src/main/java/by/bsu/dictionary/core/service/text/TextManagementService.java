package by.bsu.dictionary.core.service.text;

import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.service.word.WordManagementService;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
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
    public static String globalText = "";

    public void deleteWordFromText(String oldWord) {
        if (globalText.contains(oldWord)) {
            String tempWord = oldWord + " ";
            globalText = globalText.replaceAll(tempWord, " ");

            tempWord = " " + oldWord;
            globalText = globalText.replaceAll(tempWord, " ");
        }
        saveTextToFile(globalText);
    }

    public void replaceWordFromTextWithNewOne(String oldWord, String newWord) {
        if (globalText.contains(" "+oldWord+" ")) {
            String tempWord = oldWord + " ";
            globalText = globalText.replaceAll(tempWord, " " + newWord + " ");

            tempWord = " " + oldWord;
            globalText = globalText.replaceAll(tempWord, " " + newWord + " ");
        }
        saveTextToFile(globalText);
        saveEditedWord(oldWord, newWord);
    }

    public void addNewWord(Word word) {
        wordManagementService.save(word);
        StringBuilder stringBuilder = uploadText();
        stringBuilder.append(" ").append(word.getName());
        saveTextToFile(String.valueOf(stringBuilder));
    }

    public StringBuilder uploadText() {
        File file = new File("/home/luba/Projects/Dictionary/text.txt");
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

    public String uploadTextFromHome(MemoryBuffer memoryBuffer){
        globalText = new BufferedReader(new InputStreamReader(memoryBuffer.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        return globalText;
    }

    public void parseText() {
        String[] words = globalText.split("\\W+");
        Map<String, Long> nameFrequency = Arrays.stream(words)
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        wordManagementService.save(nameFrequency);
    }

    public void saveEditedWord(String oldWord, String newWord) {
        wordManagementService.saveEditedWord(oldWord, newWord);
    }

    @SneakyThrows
    private void saveTextToFile(String text) {
        FileWriter fileWriter = new FileWriter("savedText.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(text);
        printWriter.close();
        fileWriter.close();
    }

}
