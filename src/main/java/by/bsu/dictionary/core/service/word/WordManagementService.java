package by.bsu.dictionary.core.service.word;

import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordManagementService {

    private final WordRepository wordRepository;

    public void save(Map<String, Long> words) {
        words.forEach((key, value) -> {
            Word word = new Word();
            word.setName(key);
            word.setFrequency(value);
            wordRepository.save(word);
        });
    }

    public void delete(Word word) {
        wordRepository.delete(word);
    }

    public void save(Word word){
        wordRepository.save(word);
    }

    public void saveEditedWord(String oldWord, String newWord) {
        Optional<Word> wordOpt = wordRepository.findByName(oldWord);
        wordOpt.ifPresent(word -> {
            word.setName(newWord);
            wordRepository.save(word);
        });
    }
}
