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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TextManagementService {

    private final WordManagementService wordManagementService;
    //    private final PartOfSpeechManagementService partOfSpeechManagementService;
    private final WordRepository wordRepository;
    //    private final PartOfSpeechRepository partOfSpeechRepository;
    public static String globalText = "";

    public void deleteWordFromText(String oldWord) {

        if (globalText.contains(oldWord + "_")) {
            globalText = globalText.replaceAll("(" + oldWord + "_)([A-Z]+)\\b", " ");
        }
        saveTextToFile(globalText);
    }

    public void replaceWordFromTextWithNewOne(String oldWord, String newWord) {
        if (globalText.contains(" " + oldWord) || globalText.contains(oldWord + " ")) {
            globalText = globalText.replaceAll(oldWord, newWord);
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
    public String tokenizeSelectedText(String text) {
        globalText = tokenize(text);
        return globalText;
    }

    public boolean parseText() {
        String[] words = globalText.split("[\\s|(,)]+");
        for (String s : words) {
            System.out.println(s);
        }
        Map<String, List<String>> namePartOfSpeech = parseNameTag(Arrays.asList(words));

        Map<String, Long> nameFrequency = namePartOfSpeech.keySet().stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        wordManagementService.saveNameFrequency(nameFrequency);
        createLemmasForWord(namePartOfSpeech);
        return wordManagementService.saveNameTag(namePartOfSpeech);
    }

    public void saveEditedWord(String oldWord, String newWord) {
        wordManagementService.saveEditedWord(oldWord, newWord);
    }

    @SneakyThrows
    private void createLemmasForWord(Map<String, List<String>> nameTag) {
        String[] tokens = nameTag.keySet().toArray(new String[0]);
        String[] tags = nameTag.values().stream().map(tagList -> tagList.get(0)).toArray(String[]::new);
        System.out.println(Arrays.toString(tokens));
        System.out.println(Arrays.toString(tags));

        InputStream lemmaModelIn = new FileInputStream("en-lemmatizer.dict");
        DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(lemmaModelIn);

        String[] lemmas = lemmatizer.lemmatize(tokens, tags);
        String[] taggedLemmas = tokenize(String.join(" ", lemmas)).split(" ");

        System.out.println("tagged lemmas");
        Arrays.stream(taggedLemmas).forEach(System.out::println);

        wordManagementService.saveNameLemma(tokens, taggedLemmas); //TODO: think of how to improve it
        System.out.println(Arrays.toString(taggedLemmas));
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

    public Collection<String> getDifference(String newValue, String oldValue) {
        List<String> newValues = Arrays.asList(newValue.split(" "));
        List<String> oldValues = Arrays.asList(oldValue.split(" "));
        return CollectionUtils.subtract(newValues, oldValues);
    }

//    public void parseEditedWord(String newValue, String oldValue) {
//        Collection<String> words = getDifference(newValue, oldValue);
//        Map<String, List<String>> newNameTag = parseNameTag(words);
//        List<Word.Tags> oldTags = wordManagementService.getByName(oldValue.split("_")[0]);
//        newNameTag.forEach((newName, newTags) -> {
//            Optional<Word> wordOptional = wordRepository.findByName(newName);
//            wordOptional.ifPresent(word -> {
////                List<String> tagsByWord = word.getParts().stream().map(PartOfSpeech::getTag).collect(Collectors.toList());
////                Collection<String> coll = CollectionUtils.subtract(tags, tagsByWord);
//                if(!coll.isEmpty()){
//
//                    System.out.println("here");
//                    System.out.println("diff "+coll.toString());
//                    System.out.println("name: "+newName);
//                    System.out.println("new tags: "+newTags.toString());
//                    System.out.println("old tags: "+tagsByWord.toString());
//                    Map<String, List<String>> map = new HashMap<>();
//                    map.put(word.getName(), newTags);
////                    partOfSpeechManagementService.save(map);
//                    wordManagementService.save(word);
//                }
//            });
//        });
//
//    }

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

    public void deleteAll() {
        String[] words = globalText.split("\\W+");
        Map<String, List<String>> namePartOfSpeech = parseNameTag(Arrays.asList(words));
        namePartOfSpeech.forEach((name, value) -> {
            Optional<Word> wordOptional = wordRepository.findByName(name);
            wordOptional.ifPresent(wordRepository::delete);
        });
    }
}
