package by.bsu.dictionary.core.repository;

import by.bsu.dictionary.core.model.PartOfSpeech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartOfSpeechRepository extends JpaRepository<PartOfSpeech, Long> {

//    List<PartOfSpeech> findAllByWordId(Long wordId);

    Optional<PartOfSpeech> findByTag(String tag);
}
