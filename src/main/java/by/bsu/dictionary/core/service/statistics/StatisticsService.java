package by.bsu.dictionary.core.service.statistics;

import by.bsu.dictionary.core.model.TagFrequencyStat;
import by.bsu.dictionary.core.model.TagsFrequency;
import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.model.WordTagFrequency;
import by.bsu.dictionary.core.service.WordTagFrequencyService;
import by.bsu.dictionary.core.service.tagFrequency.TagFrequencyService;
import by.bsu.dictionary.core.service.tagsFrequency.TagsFrequencyService;
import by.bsu.dictionary.core.service.word.WordManagementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.bsu.dictionary.core.service.text.TextManagementService.globalText;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final WordManagementService wordManagementService;
    private final WordTagFrequencyService wordTagFrequencyService;
    private final TagFrequencyService tagFrequencyService;
    private final TagsFrequencyService tagsFrequencyService;

    public void tagFrequencyStatCount() {
        List<Word.Tags> tagsList = new ArrayList<>();
        wordManagementService.findAll().forEach(
                word -> tagsList.addAll(word.getTags()));
        Map<String, Long> map = tagsList.stream().collect(Collectors.groupingBy(Word.Tags::name, Collectors.counting()));
        tagFrequencyService.save(map);
    }

    public List<TagFrequencyStat> findAllTagFrequencyStat() {
        return tagFrequencyService.findAll();
    }

    public void wordTagFrequencyStatCount() {
        String[] words = globalText.split(" ");
        Map<String, Long> map = Arrays.stream(words)
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        wordTagFrequencyService.save(map);
    }

    public List<WordTagFrequency> findAllWordTagFrequency() {
        return wordTagFrequencyService.findAll();
    }

    public void tagsFrequencyStatCount() {
        List<String> tags = new ArrayList<>();
        List<String> pairTagList = new ArrayList<>();
        String[] words = globalText.split(" ");
        Arrays.stream(words).forEach(str ->
                tags.add(str.split("_")[1])
        );
        for (int i = 0; i < tags.size() - 1; i++) {
            String pairedStr = tags.get(i).concat("_").concat(tags.get(i + 1));
            pairTagList.add(pairedStr);
        }

        Map<String, Long> map = pairTagList.stream()
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        tagsFrequencyService.save(map);
    }

    public List<TagsFrequency> findAllTagsFrequency() {
        return tagsFrequencyService.findAll();
    }


}
