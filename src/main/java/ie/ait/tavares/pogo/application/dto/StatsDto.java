package ie.ait.tavares.pogo.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatsDto {

    private String pokemon;
    private String attackIv;
    private String defenseIv;
    private String hpIv;
    private String levelCap;

    public StatsDto() {
        // default
    }

    public StatsDto(String pokemon, Integer attackIv, Integer defenseIv, Integer hpIv) {
        this.pokemon = pokemon;
        this.attackIv = String.valueOf(attackIv);
        this.defenseIv = String.valueOf(defenseIv);
        this.hpIv = String.valueOf(hpIv);
    }
}
