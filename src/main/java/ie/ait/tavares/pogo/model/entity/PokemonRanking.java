package ie.ait.tavares.pogo.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class PokemonRanking {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String speciesId;
    @Column(nullable = false)
    private String speciesName;
    @Column(nullable = false)
    private Double score;
    @Column(nullable = false)
    private Double consistency;
    @Column(nullable = false)
    private String fastMove;
    @Column(nullable = false)
    private String chargedOne;
    @Column
    private String chargedTwo;
    @Column
    private String league;

}
