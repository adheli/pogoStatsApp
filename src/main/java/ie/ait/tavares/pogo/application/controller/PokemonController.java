package ie.ait.tavares.pogo.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pokemon")
public interface PokemonController {

    @GetMapping("/load")
    String createPokemonData(Model model);

    @GetMapping("/refresh")
    String refreshPokemonData(Model model);

    @GetMapping
    String getPokemonList(Model model);

    @GetMapping("/legendary")
    String getLegendary(Model model);

    @GetMapping("/shiny")
    String getShiny(Model model);

    @GetMapping("/released")
    String getReleased(Model model);
}
