package by.bsu.dictionary.core.repository;

import by.bsu.dictionary.core.model.TagsFrequency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagsFrequencyRepository extends JpaRepository<TagsFrequency, Long> {

    Optional<TagsFrequency> findByTags(String tags);
}
