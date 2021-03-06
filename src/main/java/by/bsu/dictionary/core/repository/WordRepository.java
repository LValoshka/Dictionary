package by.bsu.dictionary.core.repository;

import by.bsu.dictionary.core.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query("from Word word order by word.name")
    List<Word> findAllOrderByName();

    Optional<Word> findByName(String oldWord);
}
