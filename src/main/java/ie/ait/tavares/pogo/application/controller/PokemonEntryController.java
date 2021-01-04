package ie.ait.tavares.pogo.application.controller;

import ie.ait.tavares.pogo.application.dto.EntryDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/myPokemon")
public interface PokemonEntryController {

    @GetMapping("/add")
    String addPokemonEntryForm(Model model);

    @GetMapping("/edit/{id}")
    String editPokemonEntryForm(@PathVariable String entryId, Model model);

    @PostMapping
    ModelAndView savePokemonEntry(@ModelAttribute EntryDto entry, Model model);

    @GetMapping
    String getPokemonList(Model model);

    @GetMapping("/legendary")
    String getLegendary(Model model);

    @GetMapping("/shiny")
    String getShiny(Model model);

    @GetMapping("/league/great")
    String getGreatLeagueCP(Model model);

    @GetMapping("/league/ultra")
    String getUltraLeagueCP(Model model);

    @GetMapping("/league/master")
    String getMasterLeagueCP(Model model);

    @GetMapping("/leagues")
    String getLeagues(Model model);

    @DeleteMapping("/delete/{id}")
    ModelAndView  deletePokemonEntry(@PathVariable String entryId, Model model);
}
