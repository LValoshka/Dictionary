package by.bsu.dictionary.core.service.tagsFrequency;

import by.bsu.dictionary.core.model.TagsFrequency;
import by.bsu.dictionary.core.repository.TagsFrequencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TagsFrequencyService {

    private final TagsFrequencyRepository tagsFrequencyRepository;

    public List<TagsFrequency> findAll() {
        return tagsFrequencyRepository.findAllTags();
    }

    public void save(Map<String, Long> map) {
        map.forEach((key, value) -> {
            tagsFrequencyRepository.findByTags(key).ifPresentOrElse(entity -> {
                entity.setFrequency(value);
                save(entity);
            }, () -> {
                TagsFrequency tagsFrequency = new TagsFrequency();
                tagsFrequency.setTags(key);
                tagsFrequency.setFrequency(value);
                save(tagsFrequency);
            });
        });
    }

    public void save(TagsFrequency tagsFrequency) {
        tagsFrequencyRepository.save(tagsFrequency);
    }
}
