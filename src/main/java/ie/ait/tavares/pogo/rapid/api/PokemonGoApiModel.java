package ie.ait.tavares.pogo.rapid.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class PokemonGoApiModel {

    @JsonProperty("name")
    private String pokemonName;
    @JsonProperty("id")
    private Integer pokemonDexId;

    @JsonSerialize
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    static class Shiny extends PokemonGoApiModel {
        @JsonProperty("found_egg")
        private boolean egg;
        @JsonProperty("found_evolution")
        private boolean evolution;
        @JsonProperty("found_raid")
        private boolean raid;
        @JsonProperty("found_research")
        private boolean research;
        @JsonProperty("found_wild")
        private boolean wild;
        @JsonProperty("alolan_shiny")
        private boolean alolan;
    }

    @JsonSerialize
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    static class Rarity {
        @JsonProperty("form")
        private String form;
        @JsonProperty("rarity")
        private String rarity;
        @JsonProperty("pokemon_id")
        private Integer pokemonDexId;
        @JsonProperty("pokemon_name")
        private String pokemonName;
    }

    @JsonSerialize
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    static class RarityList {
        @JsonProperty("Legendary")
        List<Rarity> legendary;
        @JsonProperty("Mythic")
        List<Rarity> mythical;
        @JsonProperty("Standard")
        List<Rarity> standard;
    }
}
