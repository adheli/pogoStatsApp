package ie.ait.tavares.pogo.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.ait.tavares.pogo.application.LoadPvpStatsInfo;
import ie.ait.tavares.pogo.external.api.rapid.PokemonGoApiModel;
import ie.ait.tavares.pogo.external.api.rapid.PokemonGoApiService;
import ie.ait.tavares.pogo.model.entity.Pokemon;
import ie.ait.tavares.pogo.model.service.PokemonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class PokemonControllerTest {

    PokemonService service;
    PokemonGoApiService api;
    PokemonControllerImpl controller;

    Model model = new ConcurrentModel();

    @BeforeEach
    void openMocks() {
        service = mock(PokemonService.class);
        api = mock(PokemonGoApiService.class);
        controller = new PokemonControllerImpl(service, api);
    }


    @Test
    void createPokemonData() throws IOException {
        when(service.getPokemonList()).thenReturn(new ArrayList<>()).thenReturn(mockListSavedPokemon());
        when(api.getPokemonList()).thenReturn(mockOriginalPokemonList());
        when(api.getShinyPokemonList()).thenReturn(mockShinyList());
        when(api.getReleasedPokemonList()).thenReturn(mockReleasedPokemon());
        when(api.getRarityPokemonList()).thenReturn(mockRarityList());

        controller.createPokemonData(model);
        assertNotNull(model);
        assertNotNull(model.getAttribute("pokemons"));
    }

    @Test
    void getPokemonList() throws IOException {
        when(service.getPokemonList()).thenReturn(mockListSavedPokemon());

        controller.getPokemonList(model);
        assertNotNull(model);
        assertNotNull(model.getAttribute("pokemons"));
    }

    @Test
    void testGetLegendary() throws IOException {
        when(service.getLegendaryPokemonList())
                .thenReturn(mockListSavedPokemon().stream().filter(Pokemon::isLegendary).collect(Collectors.toList()));

        controller.getLegendary(model);
        assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void testGetShiny() throws IOException {
        when(service.getShinyPokemonList())
                .thenReturn(mockListSavedPokemon().stream().filter(Pokemon::isShinyReleased).collect(Collectors.toList()));

        controller.getShiny(model);
        assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        assertNotNull(list);
        assertEquals(7, list.size());
    }

    @Test
    void testGetReleased() throws IOException {
        when(service.getReleasedPokemonList())
                .thenReturn(mockListSavedPokemon().stream().filter(Pokemon::isReleased).collect(Collectors.toList()));

        controller.getReleased(model);
        assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        assertNotNull(list);
        assertEquals(11, list.size());
    }


    private Map<String, Object> getMapValues(Class<?> clazz, String jsonFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new ClassPathResource(jsonFilePath).getFile();
        return mapper.readerForMapOf(clazz).readValue(jsonFile);
    }

    private List<PokemonGoApiModel.Shiny> mockShinyList() throws IOException {
        List<PokemonGoApiModel.Shiny> shinyList = new ArrayList<>();
        getMapValues(PokemonGoApiModel.Shiny.class, "json/shiny_pokemon.json")
                .values().forEach(pkm -> shinyList.add((PokemonGoApiModel.Shiny) pkm));
        return shinyList;
    }

    private List<PokemonGoApiModel> mockOriginalPokemonList() throws IOException {
        List<PokemonGoApiModel> pokemonList = new ArrayList<>();
        getMapValues(PokemonGoApiModel.class, "json/pokemon_names.json")
                .values().forEach(pkm -> pokemonList.add((PokemonGoApiModel) pkm));
        return pokemonList;
    }

    private List<PokemonGoApiModel> mockReleasedPokemon() throws IOException {
        List<PokemonGoApiModel> releasedPokemon = new ArrayList<>();
        getMapValues(PokemonGoApiModel.class, "json/released_pokemon.json")
                .values().forEach(pkm -> releasedPokemon.add((PokemonGoApiModel) pkm));
        return releasedPokemon;
    }

    private PokemonGoApiModel.RarityList mockRarityList() throws IOException {
        File rarityFile = new ClassPathResource("json/pokemon_rarity.json").getFile();
        return new ObjectMapper().readValue(rarityFile, PokemonGoApiModel.RarityList.class);
    }

    private List<Pokemon> mockListSavedPokemon() throws IOException {
        File pokemonSaved = new ClassPathResource("json/pokemons.json").getFile();
        return new ObjectMapper().readerForListOf(Pokemon.class).readValue(pokemonSaved);
    }

    @Configuration
    public static class TestConfiguration {
        @Bean
        @Primary
        public LoadPvpStatsInfo configurationService() {
            return mock(LoadPvpStatsInfo.class);
        }
    }

}
