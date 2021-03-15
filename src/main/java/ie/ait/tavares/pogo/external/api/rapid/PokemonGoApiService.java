package ie.ait.tavares.pogo.external.api.rapid;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.ait.tavares.pogo.external.util.HttpConnectionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PokemonGoApiService {

    @Value("${pogo.api.app.key.name}")
    private String keyName;
    @Value("${pogo.api.app.key.value}")
    private String keyValue;
    @Value("${pogo.api.app.host.name}")
    private String hostName;
    @Value("${pogo.api.app.host.value}")
    private String hostValue;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String HTTPS_HOST_ADDRESS = "https://pokemon-go1.p.rapidapi.com/";
    private static final String WARN_LOADING_FILE = "Loading info from file";

    private String get(String jsonEndPoint) throws IOException {
        String url = String.join("", HTTPS_HOST_ADDRESS, jsonEndPoint);
        HttpConnectionHelper helper = new HttpConnectionHelper(url);
        HttpURLConnection connection = helper.createConnection();
        connection.setRequestProperty(keyName, new String(Base64.getDecoder().decode(keyValue)));
        connection.setRequestProperty(hostName, hostValue);
        return helper.processHttpCall(connection);
    }

    public List<PokemonGoApiModel> getPokemonList() throws IOException {
        List<PokemonGoApiModel> pokemonList = new ArrayList<>();
        Map<String, Object> pokemonMap;
        try {
            String content = get("pokemon_names.json");
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.class).readValue(content);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.warn(WARN_LOADING_FILE);

            File jsonFile = new ClassPathResource("json/pokemon_names.json").getFile();
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.class).readValue(jsonFile);
        }

        if (!pokemonMap.isEmpty()) {
            pokemonMap.values().forEach(pkm -> pokemonList.add((PokemonGoApiModel) pkm));
        }

        return pokemonList;
    }

    public List<PokemonGoApiModel.Shiny> getShinyPokemonList() throws IOException {
        List<PokemonGoApiModel.Shiny> shinyList = new ArrayList<>();
        Map<String, Object> pokemonMap;

        try {
            String content = get("shiny_pokemon.json");
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.Shiny.class).readValue(content);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.warn(WARN_LOADING_FILE);

            File jsonFile = new ClassPathResource("json/shiny_pokemon.json").getFile();
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.Shiny.class).readValue(jsonFile);
        }

        if (!pokemonMap.isEmpty()) {
            pokemonMap.values().forEach(pkm -> shinyList.add((PokemonGoApiModel.Shiny) pkm));
        }

        return shinyList;
    }

    public PokemonGoApiModel.RarityList getRarityPokemonList() throws IOException {
        try {
            String content = get("pokemon_rarity.json");
            return mapper.readValue(content, PokemonGoApiModel.RarityList.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.warn(WARN_LOADING_FILE);

            File jsonFile = new ClassPathResource("json/pokemon_rarity.json").getFile();
            return mapper.readValue(jsonFile, PokemonGoApiModel.RarityList.class);
        }
    }

    public List<PokemonGoApiModel> getReleasedPokemonList() throws IOException {
        List<PokemonGoApiModel> pokemonList = new ArrayList<>();
        Map<String, Object> pokemonMap;

        try {
            String content = get("released_pokemon.json");
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.class).readValue(content);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.warn(WARN_LOADING_FILE);

            File jsonFile = new ClassPathResource("json/released_pokemon.json").getFile();
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.class).readValue(jsonFile);
        }

        if (!pokemonMap.isEmpty()) {
            pokemonMap.values().forEach(pkm -> pokemonList.add((PokemonGoApiModel) pkm));
        }

        return pokemonList;
    }
}
