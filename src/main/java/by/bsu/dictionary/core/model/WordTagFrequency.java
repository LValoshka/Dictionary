package by.bsu.dictionary.core.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wordTagFrequencyStat")
public class WordTagFrequency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameTag;

    private Long frequency;
}
