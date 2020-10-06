package by.bsu.dictionary.core.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "words")
@Data
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long frequency;
}
