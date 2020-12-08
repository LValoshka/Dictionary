package by.bsu.dictionary.core.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "words")
@Data
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long frequency;

    private String lemma;

//    @ManyToMany(mappedBy = "words", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    private List<PartOfSpeech> parts;


    @ElementCollection(targetClass = Tags.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "word_tag", joinColumns = @JoinColumn(name = "word_id"))
    @Enumerated(EnumType.STRING)
    private Set<Tags> tags = new HashSet<>();

    public enum Tags {
        CC,
        CD,
        DT,
        EX,
        FW,
        IN,
        JJ,
        JJR,
        JJS,
        LS,
        MD,
        NN,
        NNS,
        NNP,
        NNPS,
        PDT,
        POS,
        PRP,
        PRP$,
        RB,
        RBR,
        RBS,
        RP,
        SYM,
        TO,
        UH,
        VB,
        VBD,
        VBG,
        VBN,
        VBP,
        VBZ,
        WDT,
        WP,
        WP$,
        WRB
    }
}