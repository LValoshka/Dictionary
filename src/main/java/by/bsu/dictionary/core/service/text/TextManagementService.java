package by.bsu.dictionary.core.service.text;

import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.repository.PartOfSpeechRepository;
import by.bsu.dictionary.core.repository.WordRepository;
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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TextManagementService {

    private final WordManagementService wordManagementService;
    private final PartOfSpeechManagementService partOfSpeechManagementService;
    private final WordRepository wordRepository;
    private final PartOfSpeechRepository partOfSpeechRepository;
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
        Map<String, List<String>> namePartOfSpeech = parseNameTag(Arrays.asList(words));

        Map<String, Long> nameFrequency = namePartOfSpeech.keySet().stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        wordManagementService.save(nameFrequency);
        partOfSpeechManagementService.save(namePartOfSpeech);
    }

    public void saveEditedWord(String oldWord, String newWord) {
        wordManagementService.saveEditedWord(oldWord, newWord);
    }

    public Collection<String> getDifference(String newValue, String oldValue) {
        List<String> newValues = Arrays.asList(newValue.split(" "));
        List<String> oldValues = Arrays.asList(oldValue.split(" "));
        return CollectionUtils.subtract(newValues, oldValues);
    }

    public void parseEditedWord(String newValue, String oldValue) {
        Collection<String> words = getDifference(newValue, oldValue);
        Map<String, List<String>> nameTag = parseNameTag(words);
        nameTag.forEach((name, tags) -> {
            Optional<Word> wordOptional = wordRepository.findByName(name);
//            wordOptional.ifPresentOrElse(word -> {
////                Optional<PartOfSpeech> partOfSpeechOptional = partOfSpeechRepository.findByWord(word);
//                word.set
//            }, () - < {});
        });

    }

    public Map<String, List<String>> parseNameTag(Collection<String> words) {
        Map<String, List<String>> nameTag = new HashMap<>();

        words.forEach(str -> {
            String[] parsedStr = str.split("_");
            String name = parsedStr[0];
            String partOfSpeech = parsedStr[1];

            if (nameTag.containsKey(name)) {
                nameTag.get(name).add(partOfSpeech);
            } else {
                List<String> list = new ArrayList<>();
                list.add(partOfSpeech);
                nameTag.put(name, list);
            }
        });
        nameTag.forEach((key, value) -> System.out.println("key: " + key + " value: " + value));
        return nameTag;
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
