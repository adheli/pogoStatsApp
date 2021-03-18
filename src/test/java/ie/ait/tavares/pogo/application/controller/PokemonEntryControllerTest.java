package ie.ait.tavares.pogo.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.ait.tavares.pogo.application.LoadPvpStatsInfo;
import ie.ait.tavares.pogo.application.dto.EntryDto;
import ie.ait.tavares.pogo.model.entity.Pokemon;
import ie.ait.tavares.pogo.model.entity.PokemonEntry;
import ie.ait.tavares.pogo.model.service.PokemonEntryService;
import ie.ait.tavares.pogo.model.service.PokemonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class PokemonEntryControllerTest {

    PokemonEntryService entryService;
    PokemonService pkmService;
    PokemonEntryControllerImpl controller;

    Model model = new ConcurrentModel();

    @BeforeEach
    void setup() {
        entryService = mock(PokemonEntryService.class);
        pkmService = mock(PokemonService.class);
        controller = new PokemonEntryControllerImpl(pkmService, entryService);
    }

    @Test
    void testSavePokemonEntry() {
        EntryDto entryDto = new EntryDto();
        entryDto.setPokemonId("1");
        entryDto.setCurrentCp("320");
        entryDto.setShiny(true);

        when(pkmService.getPokemon(anyInt())).thenReturn(new Pokemon());
        when(entryService.saveEntry(any())).thenReturn(new PokemonEntry());
        Assertions.assertDoesNotThrow(() -> controller.savePokemonEntry(entryDto, model));
    }

    @Test
    void testGetPokemonList() throws IOException {
        when(entryService.getEntries()).thenReturn(mockListSavedPokemon());

        controller.getPokemonList(model);
        assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        assertNotNull(list);
        assertEquals(8, list.size());
    }

    @Test
    void testGetLegendary() throws IOException {
        when(entryService.getMyLegendaries()).thenReturn(mockLegendary());

        controller.getLegendary(model);
        assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void testGetShiny() throws IOException {
        when(entryService.getMyShinies()).thenReturn(mockShiny());

        controller.getShiny(model);
        assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        assertNotNull(list);
        assertEquals(4, list.size());
    }

    @Test
    void testGreatLeagueCP() throws IOException {
        when(entryService.getGreatLeague()).thenReturn(mockGreatLeague());

        controller.getGreatLeagueCP(model);
        assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        assertNotNull(list);
        assertEquals(4, list.size());
    }

    @Test
    void testUltraLeagueCP() throws IOException {
        when(entryService.getUltraLeague()).thenReturn(mockUltraLeague());

        controller.getUltraLeagueCP(model);
        assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void testMasterLeagueCP() throws IOException {
        when(entryService.getMasterLeague()).thenReturn(mockMasterLeague());

        controller.getMasterLeagueCP(model);
        assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    private List<PokemonEntry> mockMasterLeague() throws IOException {
        return mockListSavedPokemon().stream().filter(e -> e.getCombatPower() > 2500).collect(Collectors.toList());
    }

    private List<PokemonEntry> mockListSavedPokemon() throws IOException {
        File pokemonSaved = new ClassPathResource("json/entry_list.json").getFile();
        return new ObjectMapper().readerForListOf(PokemonEntry.class).readValue(pokemonSaved);
    }

    private List<PokemonEntry> mockLegendary() throws IOException {
        return mockListSavedPokemon().stream().filter(e -> e.getPokemon().isLegendary()).collect(Collectors.toList());
    }

    private List<PokemonEntry> mockShiny() throws IOException {
        return mockListSavedPokemon().stream().filter(PokemonEntry::isShiny).collect(Collectors.toList());
    }

    private List<PokemonEntry> mockGreatLeague() throws IOException {
        return mockListSavedPokemon().stream().filter(e -> e.getCombatPower() <= 1500).collect(Collectors.toList());
    }

    private List<PokemonEntry> mockUltraLeague() throws IOException {
        return mockListSavedPokemon().stream().filter(entry -> entry.getCombatPower() > 1500 && entry.getCombatPower() <= 2500).collect(Collectors.toList());
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
