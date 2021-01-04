package ie.ait.tavares.pogo.application.dto;

import ie.ait.tavares.pogo.model.entity.PokemonEntry;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryDto {

    private String entryId;
    private String pokemonId;
    private String pokemonName;
    private String currentCp;
    private boolean shiny;

    public EntryDto() {

    }

    public EntryDto(PokemonEntry entry) {
        if (entry.getId() != null) {
            entryId = String.valueOf(entry.getId());
        }
        pokemonId = String.valueOf(entry.getPokemon().getId());
        currentCp = String.valueOf(entry.getCombatPower());
        shiny = entry.isShiny();
        pokemonName = entry.getPokemon().getName();
    }
}
