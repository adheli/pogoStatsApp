package ie.ait.tavares.pogo.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.ait.tavares.pogo.application.dto.EntryDto;
import ie.ait.tavares.pogo.model.entity.Pokemon;
import ie.ait.tavares.pogo.model.entity.PokemonEntry;
import ie.ait.tavares.pogo.model.service.PokemonEntryService;
import ie.ait.tavares.pogo.model.service.PokemonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest
@ActiveProfiles("test")
public class PokemonEntryControllerTest {
    @MockBean
    PokemonEntryService service;

    @MockBean
    PokemonService pkmService;

    @Autowired
    PokemonEntryControllerImpl controller;

    Model model = new ConcurrentModel();

    @Test
    void testSavePokemonEntry() {
        EntryDto entryDto = new EntryDto();
        entryDto.setPokemonId("1");
        entryDto.setCurrentCp("320");
        entryDto.setShiny(true);

        Mockito.when(pkmService.getPokemon(anyInt())).thenReturn(new Pokemon());
        Mockito.when(service.saveEntry(any())).thenReturn(new PokemonEntry());
        Assertions.assertDoesNotThrow(() -> controller.savePokemonEntry(entryDto, model));
    }

    @Test
    void testGetPokemonList() throws IOException {
        Mockito.when(service.getEntries()).thenReturn(mockListSavedPokemon());

        controller.getPokemonList(model);
        Assertions.assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        Assertions.assertNotNull(list);
        Assertions.assertEquals(8, list.size());
    }

    @Test
    void testGetLegendary() throws IOException {
        Mockito.when(service.getMyLegendaries()).thenReturn(mockLegendary());

        controller.getLegendary(model);
        Assertions.assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void testGetShiny() throws IOException {
        Mockito.when(service.getMyShinies()).thenReturn(mockShiny());

        controller.getShiny(model);
        Assertions.assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        Assertions.assertNotNull(list);
        Assertions.assertEquals(4, list.size());
    }

    @Test
    void testGreatLeagueCP() throws IOException {
        Mockito.when(service.getGreatLeague()).thenReturn(mockGreatLeague());

        controller.getGreatLeagueCP(model);
        Assertions.assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        Assertions.assertNotNull(list);
        Assertions.assertEquals(4, list.size());
    }

    @Test
    void testUltraLeagueCP() throws IOException {
        Mockito.when(service.getUltraLeague()).thenReturn(mockUltraLeague());

        controller.getUltraLeagueCP(model);
        Assertions.assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void testMasterLeagueCP() throws IOException {
        Mockito.when(service.getMasterLeague()).thenReturn(mockMasterLeague());

        controller.getMasterLeagueCP(model);
        Assertions.assertNotNull(model);

        List<Pokemon> list = (List<Pokemon>) model.getAttribute("pokemons");
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
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
}
