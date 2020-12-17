package by.bsu.dictionary.core.service.text;

import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.repository.WordRepository;
import by.bsu.dictionary.core.service.word.WordManagementService;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TextManagementService {

    private final WordManagementService wordManagementService;
    private final WordRepository wordRepository;
    public static String globalText = "";

    public void deleteWordFromText(String oldWord) {

        if (globalText.contains(oldWord + "_")) {
            globalText = globalText.replaceAll("(" + oldWord + "_)([A-Z]+)\\b", " ");
        }
        saveTextToFile(globalText);
    }

    public void replaceWordFromTextWithNewOne(String oldWord, String newWord) {
        if (globalText.contains(" " + oldWord + "_")) {
            globalText = globalText.replaceAll(oldWord + "_", newWord + "_");
            saveTextToFile(globalText);
            saveEditedWord(oldWord, newWord);
        }
    }

    public String uploadTextFromHome(MemoryBuffer memoryBuffer) {
        globalText = new BufferedReader(new InputStreamReader(memoryBuffer.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        return globalText;
    }

    @SneakyThrows
    public void tokenizeSelectedText(String text) {
        globalText = tokenize(text);
    }

    public boolean parseText() {
        String[] words = globalText.split("[\\s|(,)]+");

        HashMap<String, List<String>> namePartOfSpeech = parseNameTag(Arrays.asList(words));

        Map<String, Long> nameFrequency = new HashMap();
        namePartOfSpeech.forEach((key, val) -> {
            nameFrequency.put(key, (long) val.size());
        });

        wordManagementService.saveNameFrequency(nameFrequency);
        createLemmasForWord(namePartOfSpeech);
        return wordManagementService.saveNameTag(namePartOfSpeech);
    }

    public void saveEditedWord(String oldWord, String newWord) {
        wordManagementService.saveEditedWord(oldWord, newWord);
    }

    @SneakyThrows
    private void createLemmasForWord(Map<String, List<String>> nameTag) {
        Pair<String[], String[]> pair = getLemmas(nameTag);
        wordManagementService.saveNameLemma(pair.getFirst(), pair.getSecond()); //TODO: think of how to improve it
    }

    @SneakyThrows
    public Pair<String[], String[]> getLemmas(Map<String, List<String>> nameTag) {
        String[] tokens = nameTag.keySet().toArray(new String[0]);
        String[] tags = nameTag.values().stream().map(tagList -> tagList.get(0)).toArray(String[]::new);
        System.out.println(Arrays.toString(tokens));
        System.out.println(Arrays.toString(tags));

        InputStream lemmaModelIn = new FileInputStream("en-lemmatizer.dict");
        DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(lemmaModelIn);

        String[] lemmas = lemmatizer.lemmatize(tokens, tags);
        String[] taggedLemmas = tokenize(String.join(" ", lemmas)).split(" ");
        return Pair.of(tokens, taggedLemmas);
    }

    @SneakyThrows
    private String tokenize(String text) {
        InputStream posModelIn = new FileInputStream("en-pos-maxent.bin");
        POSModel model = new POSModel(posModelIn);
        POSTagger tagger = new POSTaggerME(model);

        WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
        String[] tokens = whitespaceTokenizer.tokenize(text);
        String[] tags = tagger.tag(tokens);

        POSSample sample = new POSSample(tokens, tags);
        return sample.toString();
    }

    public HashMap<String, List<String>> parseNameTag(Collection<String> words) {
        HashMap<String, List<String>> nameTag = new HashMap<>();

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

    public void deleteAll() {
        String[] words = globalText.split("\\W+");
        Map<String, List<String>> namePartOfSpeech = parseNameTag(Arrays.asList(words));
        namePartOfSpeech.forEach((name, value) -> {
            Optional<Word> wordOptional = wordRepository.findByName(name);
            wordOptional.ifPresent(wordRepository::delete);
        });
    }
}
