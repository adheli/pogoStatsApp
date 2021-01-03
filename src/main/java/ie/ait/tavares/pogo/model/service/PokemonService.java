package ie.ait.tavares.pogo.model.service;

import ie.ait.tavares.pogo.model.dao.PokemonDao;
import ie.ait.tavares.pogo.model.entity.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return pokemonDao.findAll();
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

    public void deletePokemon(Integer pokemonId) {
        pokemonDao.deleteById(pokemonId);
    }
}
