package by.bsu.dictionary.core.repository;

import by.bsu.dictionary.core.model.TagFrequencyStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagFrequencyStatRepository extends JpaRepository<TagFrequencyStat, Long> {
    Optional<TagFrequencyStat> findByTag(String key);

    @Query("from TagFrequencyStat where tag is not null")
    List<TagFrequencyStat> findAllTag();
}
