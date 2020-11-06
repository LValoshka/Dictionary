package by.bsu.dictionary.core.service.word;

import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordFinder {

    private final WordRepository wordRepository;

    public List<Word> findAll() {
        return wordRepository.findAllOrderByName();
    }

    public List<Word> findAllByFilter(String filter) {
        if(filter.isEmpty()) {
            return wordRepository.findAllOrderByName();
        } else {
            return wordRepository.findAllByFilter(filter);
        }
    }
}
