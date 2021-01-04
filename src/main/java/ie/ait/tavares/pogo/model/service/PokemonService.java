package ie.ait.tavares.pogo.model.service;

import ie.ait.tavares.pogo.model.dao.PokemonDao;
import ie.ait.tavares.pogo.model.entity.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {

    private final PokemonDao pokemonDao;

    @Autowired
    public PokemonService(PokemonDao pokemonDao) {
        this.pokemonDao = pokemonDao;
    }

    public Pokemon savePokemon(Pokemon pokemon) {
        return pokemonDao.saveAndFlush(pokemon);
    }

    public Pokemon getPokemon(Integer pokemonId) {
        return pokemonDao.findById(pokemonId).orElse(null);
    }

    public List<Pokemon> getPokemonList() {
        return Optional.of(pokemonDao.findAll()).orElse(new ArrayList<>());
    }

    public List<Pokemon> getLegendaryPokemonList() {
        return pokemonDao.findLegendary();
    }

    public List<Pokemon> getShinyPokemonList() {
        return pokemonDao.findShiny();
    }

    public List<Pokemon> getReleasedPokemonList() {
        return pokemonDao.findReleased();
    }
}
