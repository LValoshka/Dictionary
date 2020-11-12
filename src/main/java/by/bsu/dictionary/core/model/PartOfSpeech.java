package by.bsu.dictionary.core.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "parts")
@Data
public class PartOfSpeech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(joinColumns = {@JoinColumn(name = "part_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "word_id", referencedColumnName = "id")})
    private List<Word> words;

//    public enum Tags {
//        N, DT, VB, VBD, VBZ, IN, NNP, TO, JJ
//    }

}