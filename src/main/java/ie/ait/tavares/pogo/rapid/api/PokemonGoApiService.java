package ie.ait.tavares.pogo.rapid.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
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

    private String processHttpCall(String jsonEndPoint) throws IOException {
        HttpURLConnection connection = null;
        try {
            String url = String.join("", HTTPS_HOST_ADDRESS, jsonEndPoint);
            log.info("Request sent to URL: {}", url);
            connection = createConnection(url);
            connection.setRequestProperty(keyName, new String(Base64.getDecoder().decode(keyValue)));
            connection.setRequestProperty(hostName, hostValue);

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            Reader streamReader;

            log.info("Response headers: {}", connection.getHeaderFields());
            log.info("Response code: {}", connection.getResponseCode());

            if (connection.getResponseCode() > 299) {
                streamReader = new InputStreamReader(connection.getErrorStream());
                throw new InvalidObjectException(getResponse(streamReader));
            } else {
                streamReader = new InputStreamReader(connection.getInputStream());
                return getResponse(streamReader);
            }

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String getResponse(Reader streamReader) throws IOException {
        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        log.info("Response body: {}", content.toString());
        return content.toString();
    }

    private HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection;
    }

    public List<PokemonGoApiModel> getPokemonList() throws IOException {
        List<PokemonGoApiModel> pokemonList = new ArrayList<>();
        Map<String, Object> pokemonMap;
        try {
            String content = processHttpCall("pokemon_names.json");
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.class).readValue(content);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.warn("Loading info from file");

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
            String content = processHttpCall("shiny_pokemon.json");
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.Shiny.class).readValue(content);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.warn("Loading info from file");

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
            String content = processHttpCall("pokemon_rarity.json");
            return mapper.readValue(content, PokemonGoApiModel.RarityList.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.warn("Loading info from file");

            File jsonFile = new ClassPathResource("json/pokemon_rarity.json").getFile();
            return mapper.readValue(jsonFile, PokemonGoApiModel.RarityList.class);
        }
    }

    public List<PokemonGoApiModel> getReleasedPokemonList() throws IOException {
        List<PokemonGoApiModel> pokemonList = new ArrayList<>();
        Map<String, Object> pokemonMap;

        try {
            String content = processHttpCall("released_pokemon.json");
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.class).readValue(content);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.warn("Loading info from file");

            File jsonFile = new ClassPathResource("json/released_pokemon.json").getFile();
            pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.class).readValue(jsonFile);
        }

        if (!pokemonMap.isEmpty()) {
            pokemonMap.values().forEach(pkm -> pokemonList.add((PokemonGoApiModel) pkm));
        }

        return pokemonList;
    }
}
