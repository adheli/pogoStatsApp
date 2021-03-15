package ie.ait.tavares.pogo.external.api.pvppoke;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class PvpPokeModel {

    private String speciesId;
    private String speciesName;
    private Double score;
    private Integer rating;
    private List<String> moveset;
    private List<Double> scores;
    private List<OpponentPokemon> counters;
    private List<OpponentPokemon> matchups;
    private Moves moves;

    @JsonSerialize
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class OpponentPokemon {
        private String opponent;
        private Integer rating;
    }

    @JsonSerialize
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Move {
        private String moveId;
        private Integer uses;
    }

    @JsonSerialize
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    public static class Moves {
        private List<Move> chargedMoves;
        private List<Move> fastMoves;
    }
}
