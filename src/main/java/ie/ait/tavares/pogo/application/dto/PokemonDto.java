package ie.ait.tavares.pogo.application.dto;

import ie.ait.tavares.pogo.model.entity.Pokemon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokemonDto {

    private Integer dexEntry;
    private String name;
    private boolean legendary;
    private boolean released;
    private boolean shinyReleased;

    public PokemonDto() {
        // normal constructor
    }

    public PokemonDto(Pokemon pokemon) {
        this.dexEntry = pokemon.getDexEntry();
        this.name = pokemon.getName();
        this.legendary = pokemon.isLegendary();
        this.released = pokemon.isReleased();
        this.shinyReleased = pokemon.isShinyReleased();
    }
}
