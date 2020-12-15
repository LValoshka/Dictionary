package by.bsu.dictionary.core.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tagFrequencyStat")
public class TagFrequencyStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    private Long frequency;

}
