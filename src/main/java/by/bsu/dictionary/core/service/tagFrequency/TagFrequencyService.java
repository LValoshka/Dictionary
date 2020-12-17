package by.bsu.dictionary.core.service.tagFrequency;

import by.bsu.dictionary.core.model.TagFrequencyStat;
import by.bsu.dictionary.core.repository.TagFrequencyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TagFrequencyService {

    private final TagFrequencyStatRepository tagFrequencyStatRepository;

    public List<TagFrequencyStat> findAll() {
        return tagFrequencyStatRepository.findAllTag();
    }

    public void save(Map<String, Long> map) {
        map.forEach((key, value) -> {
            tagFrequencyStatRepository.findByTag(key).ifPresentOrElse(entity -> {
                entity.setFrequency(value);
                save(entity);
            }, () -> {
                TagFrequencyStat tagFrequencyStat = new TagFrequencyStat();
                tagFrequencyStat.setTag(key);
                tagFrequencyStat.setFrequency(value);
                save(tagFrequencyStat);
            });
        });
    }

    public void save(TagFrequencyStat tagFrequencyStat) {
        tagFrequencyStatRepository.save(tagFrequencyStat);
    }

}
