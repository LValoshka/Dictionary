package by.bsu.dictionary.core.service.word;

import by.bsu.dictionary.core.model.Word;
import by.bsu.dictionary.core.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordManagementService {

    private final WordRepository wordRepository;

    public void saveNameFrequency(Map<String, Long> words) {
        words.forEach((key, value) -> {
            Word word = new Word();
            word.setName(key);
            word.setFrequency(value);
            wordRepository.save(word);
        });
    }

    public boolean saveNameTag(Map<String, List<String>> words) {
        AtomicBoolean res = new AtomicBoolean(true);
        words.forEach((key, value) -> {
            wordRepository.findByName(key).ifPresent(word -> {
                word.setName(key);
                try {
                    Set<Word.Tags> tags = value.stream().map(Word.Tags::valueOf).collect(Collectors.toSet());
                    word.setTags(tags);
                    wordRepository.save(word);
                } catch (RuntimeException e) {
                    res.set(false);
                    log.error("Can't parse new string");
                }
            });
        });
        return res.get();
    }

    public void saveEditedWord(String oldWord, String newWord) {
        Optional<Word> wordOpt = wordRepository.findByName(oldWord);
        wordOpt.ifPresent(word -> {
            word.setName(newWord);
            wordRepository.save(word);
        });
    }

    public void saveNameLemma(String[] tokens, String[] lemmas) { //TODO: REWRITE IT, PLEASE!!!
        for (int i = 0; i < tokens.length; i++) {
            Word word = getByName(tokens[i]);
            word.setLemma(lemmas[i]);
            save(word);
        }
    }

    public void save(Word word) {
        wordRepository.save(word);
    }

    public void delete(Word word) {
        wordRepository.delete(word);
    }

    public Word getByName(String name) {
        return wordRepository.findByName(name).orElseThrow();
    }

    public List<Word> findAll() {
        return wordRepository.findAll();
    }

    public void saveWordWithNewLemma(String name, String lemma) {
        Word word = getByName(name);
        word.setLemma(lemma);
        save(word);
    }
}
