package ie.ait.tavares.pogo.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class ApiInfo {

    @Id
    @GeneratedValue
    private Integer id;

    private String pvpPokeApiVersion;
}
