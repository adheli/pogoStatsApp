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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public interface PokemonEntryController {

    @GetMapping("myPokemon/add")
    String addPokemonEntryForm(Model model);

    @GetMapping("myPokemon/edit/{entryId}")
    String editPokemonEntryForm(@PathVariable String entryId, Model model);

    @PostMapping("myPokemon")
    ModelAndView savePokemonEntry(@ModelAttribute EntryDto entry, Model model);

    @GetMapping("myPokemon")
    String getPokemonList(Model model);

    @GetMapping("myPokemon/legendary")
    String getLegendary(Model model);

    @GetMapping("myPokemon/shiny")
    String getShiny(Model model);

    @GetMapping("leagues/great")
    String getGreatLeagueCP(Model model);

    @GetMapping("leagues/ultra")
    String getUltraLeagueCP(Model model);

    @GetMapping("leagues/master")
    String getMasterLeagueCP(Model model);

    @GetMapping("leagues")
    String getLeagues(Model model);

    @GetMapping("myPokemon/delete/{entryId}")
    ModelAndView deletePokemonEntry(@PathVariable String entryId, RedirectAttributes redirAttrs);
}
