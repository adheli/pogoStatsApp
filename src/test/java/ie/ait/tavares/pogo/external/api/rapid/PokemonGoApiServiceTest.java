package ie.ait.tavares.pogo.external.api.rapid;

import ie.ait.tavares.pogo.application.LoadPvpStatsInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class PokemonGoApiServiceTest {

    @Autowired
    PokemonGoApiService apiService;

    @Test
    void checkServiceWorking() throws IOException {
        if (apiService == null)
            apiService = new PokemonGoApiService();

        List<PokemonGoApiModel> pokemonList = apiService.getPokemonList();
        assertNotNull(pokemonList);

        List<PokemonGoApiModel.Shiny> shinyPokemonList = apiService.getShinyPokemonList();
        assertNotNull(shinyPokemonList);

        PokemonGoApiModel.RarityList rarityPokemonList = apiService.getRarityPokemonList();
        assertNotNull(rarityPokemonList);

        List<PokemonGoApiModel> releasedPokemon = apiService.getReleasedPokemonList();
        assertNotNull(releasedPokemon);
    }

    @Configuration
    public static class TestConfiguration {
        @Bean
        @Primary
        public LoadPvpStatsInfo configurationService() {
            return Mockito.mock(LoadPvpStatsInfo.class);
        }

        @Bean
        public PokemonGoApiService pokemonGoApiService() {
            return new PokemonGoApiService();
        }
    }
}
