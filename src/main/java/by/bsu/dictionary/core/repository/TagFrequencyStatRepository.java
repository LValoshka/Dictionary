package by.bsu.dictionary.core.repository;

import by.bsu.dictionary.core.model.TagFrequencyStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagFrequencyStatRepository extends JpaRepository<TagFrequencyStat, Long> {
    Optional<TagFrequencyStat> findByTag(String key);
}
