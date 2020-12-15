package by.bsu.dictionary.core.service;

import by.bsu.dictionary.core.model.WordTagFrequency;
import by.bsu.dictionary.core.repository.WordTagFrequencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WordTagFrequencyService {

    private final WordTagFrequencyRepository wordTagFrequencyRepository;

    public List<WordTagFrequency> findAll() {
        return wordTagFrequencyRepository.findAll();
    }

    public void save(Map<String, Long> map) {
        map.forEach((key, value) -> {
            wordTagFrequencyRepository.findByNameTag(key).ifPresentOrElse(entity -> {
                entity.setFrequency(value);
                save(entity);
            }, () -> {
                WordTagFrequency wordTagFrequency = new WordTagFrequency();
                wordTagFrequency.setNameTag(key);
                wordTagFrequency.setFrequency(value);
                save(wordTagFrequency);
            });
        });
    }

    public void save(WordTagFrequency wordTagFrequency) {
        wordTagFrequencyRepository.save(wordTagFrequency);
    }
}
