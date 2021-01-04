package ie.ait.tavares.pogo.application.controller;

import ie.ait.tavares.pogo.application.dto.EntryDto;
import ie.ait.tavares.pogo.model.entity.Pokemon;
import ie.ait.tavares.pogo.model.entity.PokemonEntry;
import ie.ait.tavares.pogo.model.service.PokemonEntryService;
import ie.ait.tavares.pogo.model.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class PokemonEntryControllerImpl implements PokemonEntryController {

    private final PokemonService pokemonService;
    private final PokemonEntryService entryService;

    private final static String ENTRY_FORM = "entryForm";
    private final static String LIST_PAGE = "myPokemonList";
    private final static String LEAGUES_PAGE = "leagues";

    @Autowired
    public PokemonEntryControllerImpl(PokemonService pokemonService, PokemonEntryService entryService) {
        this.pokemonService = pokemonService;
        this.entryService = entryService;
    }

    @Override
    public String addPokemonEntryForm(Model model) {
        model.addAttribute("action", "Add");
        model.addAttribute("entry", new EntryDto());
        model.addAttribute("pokemons", pokemonService.getReleasedPokemonList());
        return ENTRY_FORM;
    }

    @Override
    public String editPokemonEntryForm(String entryId, Model model) {
        model.addAttribute("action", "Edit");
        PokemonEntry entry = entryService.getEntry(Integer.valueOf(entryId));
        model.addAttribute("entry", new EntryDto(entry));
        model.addAttribute("pokemons", pokemonService.getReleasedPokemonList());
        return ENTRY_FORM;
    }

    @Override
    public ModelAndView savePokemonEntry(EntryDto entry, Model model) {
        saveEntry(entry);

        return redirectMyPokemonList();
    }

    @Override
    public String getPokemonList(Model model) {
        List<EntryDto> pokemons = new ArrayList<>();
        entryService.getEntries().forEach(entry1 -> pokemons.add(new EntryDto(entry1)));
        model.addAttribute("pokemons", pokemons);
        return LIST_PAGE;
    }

    @Override
    public String getLegendary(Model model) {
        List<EntryDto> pokemons = new ArrayList<>();
        entryService.getMyLegendaries().forEach(entry1 -> pokemons.add(new EntryDto(entry1)));
        model.addAttribute("pokemons", pokemons);
        return LIST_PAGE;
    }

    @Override
    public String getShiny(Model model) {
        List<EntryDto> pokemons = new ArrayList<>();
        entryService.getMyShinies().forEach(entry1 -> pokemons.add(new EntryDto(entry1)));
        model.addAttribute("pokemons", pokemons);
        return LIST_PAGE;
    }

    @Override
    public String getGreatLeagueCP(Model model) {
        List<EntryDto> pokemons = new ArrayList<>();
        entryService.getGreatLeague().forEach(entry1 -> pokemons.add(new EntryDto(entry1)));
        model.addAttribute("pokemons", pokemons);
        return LEAGUES_PAGE;
    }

    @Override
    public String getUltraLeagueCP(Model model) {
        List<EntryDto> pokemons = new ArrayList<>();
        entryService.getUltraLeague().forEach(entry1 -> pokemons.add(new EntryDto(entry1)));
        model.addAttribute("pokemons", pokemons);
        return LEAGUES_PAGE;
    }

    @Override
    public String getMasterLeagueCP(Model model) {
        List<EntryDto> pokemons = new ArrayList<>();
        entryService.getMasterLeague().forEach(entry1 -> pokemons.add(new EntryDto(entry1)));
        model.addAttribute("pokemons", pokemons);
        return LEAGUES_PAGE;
    }

    @Override
    public String getLeagues(Model model) {
        return LEAGUES_PAGE;
    }

    @Override
    public ModelAndView deletePokemonEntry(String entryId, Model model) {
        entryService.deleteEntry(Integer.valueOf(entryId));

        return redirectMyPokemonList();
    }

    private void saveEntry(EntryDto entry) {
        PokemonEntry pokemonEntry = new PokemonEntry();
        pokemonEntry.setCombatPower(Integer.valueOf(entry.getCurrentCp()));
        pokemonEntry.setShiny(entry.isShiny());

        Pokemon pokemon = pokemonService.getPokemon(Integer.valueOf(entry.getPokemonId()));
        pokemonEntry.setPokemon(pokemon);

        if (entry.getEntryId() != null) {
            pokemonEntry.setId(Integer.valueOf(entry.getEntryId()));
        }

        entryService.saveEntry(pokemonEntry);
    }

    private ModelAndView redirectMyPokemonList() {
        List<EntryDto> pokemons = new ArrayList<>();
        entryService.getEntries().forEach(e -> pokemons.add(new EntryDto(e)));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("pokemons", pokemons);

        return new ModelAndView("redirect:/myPokemon", attributes);
    }
}
