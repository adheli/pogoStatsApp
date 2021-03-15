package ie.ait.tavares.pogo.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity
public class Pokemon {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, unique = true)
    private Integer dexEntry;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean legendary;

    @Column(nullable = false)
    private boolean released;

    @Column(nullable = false)
    private boolean shinyReleased;;

}
