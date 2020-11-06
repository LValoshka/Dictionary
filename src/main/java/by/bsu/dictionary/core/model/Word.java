package by.bsu.dictionary.core.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "words")
@Data
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long frequency;

    @ManyToMany(mappedBy = "words", fetch = FetchType.EAGER)
    private List<PartOfSpeech> parts;
}
