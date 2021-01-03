package ie.ait.tavares.pogo.rapid.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
public class PokemonGoApiServiceTest {

    @Autowired
    PokemonGoApiService apiService;

    @Test
    void checkServiceWorking() {
        if (apiService == null)
            apiService = new PokemonGoApiService();

        List<PokemonGoApiModel> pokemonList = apiService.getPokemonList();
        Assertions.assertNotNull(pokemonList);

        List<PokemonGoApiModel.Shiny> shinyPokemonList = apiService.getShinyPokemonList();
        Assertions.assertNotNull(shinyPokemonList);

        PokemonGoApiModel.RarityList rarityPokemonList = apiService.getRarityPokemonList();
        Assertions.assertNotNull(rarityPokemonList);
    }
}
