package by.bsu.dictionary.core.service.text;

import by.bsu.dictionary.core.service.part_of_speech.PartOfSpeechManagementService;
import by.bsu.dictionary.core.service.word.WordManagementService;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TextManagementService {

    private final WordManagementService wordManagementService;
    private final PartOfSpeechManagementService partOfSpeechManagementService;
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
        if (globalText.contains(" " + oldWord + " ")) {
            String tempWord = oldWord + " ";
            globalText = globalText.replaceAll(tempWord, " " + newWord + " ");

            tempWord = " " + oldWord;
            globalText = globalText.replaceAll(tempWord, " " + newWord + " ");
        }
        saveTextToFile(globalText);
        saveEditedWord(oldWord, newWord);
    }

    public StringBuilder uploadText() {
        File file = new File("/home/luba/IdeaProjects/Dictionary/text.txt");
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

    public String uploadTextFromHome(MemoryBuffer memoryBuffer) {
        globalText = new BufferedReader(new InputStreamReader(memoryBuffer.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        return globalText;
    }

    @SneakyThrows
    public String tokenizeText(String text) {
        InputStream inputStream = new FileInputStream("/home/luba/IdeaProjects/Dictionary/en-pos-maxent.bin");
        POSModel model = new POSModel(inputStream);
        POSTagger tagger = new POSTaggerME(model);

        WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
        String[] tokens = whitespaceTokenizer.tokenize(text);

        String[] tags = tagger.tag(tokens);

        POSSample sample = new POSSample(tokens, tags);

        globalText = sample.toString();
        return globalText;
    }

    public void parseText() {
        String[] words = globalText.split("\\W+");
        Map<String, List<String>> namePartOfSpeech = new HashMap<>();
        Arrays.stream(words).forEach(str -> {
            String[] parsedStr = str.split("_");
            String name = parsedStr[0];
            String partOfSpeech = parsedStr[1];

            namePartOfSpeech.put(name, Collections.singletonList(partOfSpeech));
        });

        Map<String, Long> nameFrequency = namePartOfSpeech.keySet().stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        wordManagementService.save(nameFrequency);
        partOfSpeechManagementService.save(namePartOfSpeech);
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
