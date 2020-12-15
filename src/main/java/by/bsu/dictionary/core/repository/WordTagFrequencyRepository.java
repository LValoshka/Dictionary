package by.bsu.dictionary.core.repository;

import by.bsu.dictionary.core.model.WordTagFrequency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordTagFrequencyRepository extends JpaRepository<WordTagFrequency, Long> {

    Optional<WordTagFrequency> findByNameTag(String nameTag);
}
