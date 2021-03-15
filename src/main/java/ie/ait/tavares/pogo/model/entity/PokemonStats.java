package ie.ait.tavares.pogo.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class PokemonStats {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Integer dex;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer attack;
    @Column(nullable = false)
    private Integer defense;
    @Column(nullable = false)
    private Integer healthPoints;

    @Override
    public String toString() {
        return "PokemonStats{" +
                "id=" + id +
                ", dex=" + dex +
                ", name='" + name + '\'' +
                ", attack=" + attack +
                ", defense=" + defense +
                ", healthPoints=" + healthPoints +
                '}';
    }
}
