package by.bsu.dictionary.core.service.statistics;

import by.bsu.dictionary.core.model.TagFrequencyStat;
import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.service.tagFrequency.TagFrequencyService;
import by.bsu.dictionary.core.service.word.WordManagementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final WordManagementService wordManagementService;

    private final TagFrequencyService tagFrequencyService;

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

}
