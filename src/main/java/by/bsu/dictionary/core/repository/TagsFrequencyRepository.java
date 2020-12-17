package by.bsu.dictionary.core.repository;

import by.bsu.dictionary.core.model.TagsFrequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagsFrequencyRepository extends JpaRepository<TagsFrequency, Long> {


    List<TagsFrequency> findAll();

    @Query("from TagsFrequency where tags is not null")
    List<TagsFrequency> findAllTags();

    Optional<TagsFrequency> findByTags(String tags);
}
