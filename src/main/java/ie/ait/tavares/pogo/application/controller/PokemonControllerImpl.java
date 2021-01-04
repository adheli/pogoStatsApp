package ie.ait.tavares.pogo.application.controller;

import ie.ait.tavares.pogo.application.dto.PokemonDto;
import ie.ait.tavares.pogo.model.entity.Pokemon;
import ie.ait.tavares.pogo.model.service.PokemonService;
import ie.ait.tavares.pogo.rapid.api.PokemonGoApiModel;
import ie.ait.tavares.pogo.rapid.api.PokemonGoApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class PokemonControllerImpl implements PokemonController {

    private final PokemonService service;
    private final PokemonGoApiService api;

    private static final String POKEMON_LIST_PAGE = "pokemonList";

    @Autowired
    public PokemonControllerImpl(PokemonService service, PokemonGoApiService api) {
        this.service = service;
        this.api = api;
    }

    @Override
    public String createPokemonData(Model model) {
        log.info("Loading data from API");
        try {
            List<PokemonDto> pokemons = getPokemons();
            if (pokemons.isEmpty()) {
                loadPokemonFromApi();
                pokemons = getPokemons();
            }
            model.addAttribute("pokemons", pokemons);
            return "redirect:/pokemon";
        } catch (IOException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @Override
    public String refreshPokemonData(Model model) {
        List<PokemonDto> pokemons = getPokemons();
        model.addAttribute("pokemons", pokemons);
        return POKEMON_LIST_PAGE;
    }

    @Override
    public String getPokemonList(Model model) {
        List<PokemonDto> pokemons = getPokemons();
        model.addAttribute("pokemons", pokemons);
        return POKEMON_LIST_PAGE;
    }

    @Override
    public String getLegendary(Model model) {
        List<PokemonDto> pokemons = getLegendaryPokemons();
        model.addAttribute("pokemons", pokemons);
        return POKEMON_LIST_PAGE;
    }

    @Override
    public String getShiny(Model model) {
        List<PokemonDto> pokemons = getShinyPokemons();
        model.addAttribute("pokemons", pokemons);
        return POKEMON_LIST_PAGE;
    }

    @Override
    public String getReleased(Model model) {
        List<PokemonDto> pokemons = getOnlyReleasedPokemons();
        model.addAttribute("pokemons", pokemons);
        return POKEMON_LIST_PAGE;
    }

    protected void loadPokemonFromApi() throws IOException {
        List<Pokemon> pokemons = new ArrayList<>();

        List<PokemonGoApiModel> pokemonList = api.getPokemonList();
        List<PokemonGoApiModel.Shiny> shinyList = api.getShinyPokemonList();
        PokemonGoApiModel.RarityList rarityList = api.getRarityPokemonList();
        List<PokemonGoApiModel> releasedList = api.getReleasedPokemonList();

        log.info("Found {} pokemons", pokemonList.size());
        log.info("Found {} shiny pokemons", shinyList.size());
        log.info("Found {} released pokemons", releasedList.size());

        if (!pokemonList.isEmpty() && !shinyList.isEmpty() && !releasedList.isEmpty() && rarityList != null) {

            List<PokemonGoApiModel.Rarity> legendaryList = new ArrayList<>();
            legendaryList.addAll(rarityList.getLegendary());
            legendaryList.addAll(rarityList.getMythical());
            log.info("Found {} legendary pokemons", legendaryList.size());

            pokemonList.forEach(pokemon -> {
                boolean isLegendary = legendaryList.stream().anyMatch(legendary -> legendary.getPokemonDexId().equals(pokemon.getPokemonDexId()));
                boolean isShiny = shinyList.stream().anyMatch(shiny -> shiny.getPokemonDexId().equals(pokemon.getPokemonDexId()) && shiny.isOriginalShiny());
                boolean isReleased = releasedList.stream().anyMatch(released -> released.getPokemonDexId().equals(pokemon.getPokemonDexId()));

                pokemons.add(mapApiToEntity(pokemon, isLegendary, isShiny, isReleased));
            });
        }

        if (!pokemons.isEmpty()) {
            log.info("Saving pokemon data to PoGo Stats application");
            pokemons.forEach(service::savePokemon);
        }
    }

    private Pokemon mapApiToEntity(PokemonGoApiModel apiModel, boolean isLegendary, boolean isShiny, boolean isReleased) {
        Pokemon pokemon = new Pokemon();

        pokemon.setDexEntry(apiModel.getPokemonDexId());
        pokemon.setName(apiModel.getPokemonName());
        pokemon.setLegendary(isLegendary);
        pokemon.setShinyReleased(isShiny);
        pokemon.setReleased(isReleased);

        return pokemon;
    }

    private List<PokemonDto> getPokemons(boolean all, boolean onlyLegendary, boolean onlyReleased, boolean onlyShiny) {
        List<PokemonDto> list = new ArrayList<>();

        if (all) {
            service.getPokemonList().forEach(p -> list.add(new PokemonDto(p)));
        } else {
            if (onlyLegendary) {
                service.getLegendaryPokemonList().forEach(p -> list.add(p.getDexEntry(), new PokemonDto(p)));
            } else if (onlyReleased) {
                service.getReleasedPokemonList().forEach(p -> list.add(p.getDexEntry(), new PokemonDto(p)));
            } else if (onlyShiny) {
                service.getShinyPokemonList().forEach(p -> list.add(p.getDexEntry(), new PokemonDto(p)));
            }
        }

        return list;
    }

    private List<PokemonDto> getPokemons() {
        return getPokemons(true, false, false, false);
    }

    private List<PokemonDto> getShinyPokemons() {
        return getPokemons(false, false, true, true);
    }

    private List<PokemonDto> getLegendaryPokemons() {
        return getPokemons(false, true, true, false);
    }

    private List<PokemonDto> getOnlyReleasedPokemons() {
        return getPokemons(false, false, true, false);
    }
}
