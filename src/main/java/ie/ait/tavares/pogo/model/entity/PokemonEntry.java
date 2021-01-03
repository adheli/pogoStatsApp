package ie.ait.tavares.pogo.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class PokemonEntry {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer combatPower;

    @Column
    private boolean shiny;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;
}
