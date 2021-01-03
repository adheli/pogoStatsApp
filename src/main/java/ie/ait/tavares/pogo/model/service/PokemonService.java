package ie.ait.tavares.pogo.model.service;

import ie.ait.tavares.pogo.model.dao.PokemonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private final PokemonDao pokemonDao;

    @Autowired
    public PokemonService(PokemonDao pokemonDao) {
        this.pokemonDao = pokemonDao;
    }
}
