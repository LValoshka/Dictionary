package by.bsu.dictionary.core.service.part_of_speech;

import by.bsu.dictionary.core.model.PartOfSpeech;
import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.repository.PartOfSpeechRepository;
import by.bsu.dictionary.core.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartOfSpeechManagementService {

    private final PartOfSpeechRepository partOfSpeechRepository;
    private final WordRepository wordRepository;


    public void save(Map<String, List<String>> map) {
        map.forEach((key, value) -> {
            Optional<Word> wordOptional = wordRepository.findByName(key);
            wordOptional.ifPresent(word -> value.forEach(tag -> {
                Optional<PartOfSpeech> part = partOfSpeechRepository.findByTag(tag);
                part.ifPresentOrElse(partOfSpeech -> {
                    partOfSpeech.getWords().add(word);
                    word.getParts().add(partOfSpeech);
                    partOfSpeechRepository.save(partOfSpeech);
                    wordRepository.save(word);
                }, () -> {
                    PartOfSpeech partOfSpeech = new PartOfSpeech();
                    partOfSpeech.setWords(Collections.singletonList(word));
                    partOfSpeech.setTag(tag);
                    save(partOfSpeech);
                    word.setParts(Collections.singletonList(partOfSpeech));
                    wordRepository.save(word);
                });
            }));
        });
    }

    public void save(PartOfSpeech partOfSpeech) {
        partOfSpeechRepository.save(partOfSpeech);
    }
}
