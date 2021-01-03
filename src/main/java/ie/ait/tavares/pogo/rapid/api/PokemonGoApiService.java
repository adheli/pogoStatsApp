package ie.ait.tavares.pogo.rapid.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
            log.debug("Request sent to URL: {}", url);
            connection = createConnection(url);
            connection.setRequestProperty(keyName, keyValue);
            connection.setRequestProperty(hostName, hostValue);

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            Reader streamReader;

            log.debug("Response headers: {}", connection.getHeaderFields());
            log.debug("Response code: {}", connection.getResponseCode());

            if (connection.getResponseCode() > 299) {
                streamReader = new InputStreamReader(connection.getErrorStream());
            } else {
                streamReader = new InputStreamReader(connection.getInputStream());
            }


            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            log.debug("Response body: {}", content.toString());
            return content.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection;
    }

    public List<PokemonGoApiModel> getPokemonList() {
        List<PokemonGoApiModel> pokemonList = new ArrayList<>();
        try {
            String content = processHttpCall("pokemon_names.json");
            Map<String, Object> pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.class).readValue(content);
            if (!pokemonMap.isEmpty()) {
                pokemonMap.values().forEach(pkm -> pokemonList.add((PokemonGoApiModel) pkm));
            }

            return pokemonList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PokemonGoApiModel.Shiny> getShinyPokemonList() {
        List<PokemonGoApiModel.Shiny> shinyList = new ArrayList<>();
        try {
            String content = processHttpCall("shiny_pokemon.json");
            Map<String, Object> pokemonMap = mapper.readerForMapOf(PokemonGoApiModel.Shiny.class).readValue(content);
            if (!pokemonMap.isEmpty()) {
                pokemonMap.values().forEach(pkm -> shinyList.add((PokemonGoApiModel.Shiny) pkm));
            }

            return shinyList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PokemonGoApiModel.RarityList getRarityPokemonList() {
        try {
            String content = processHttpCall("pokemon_rarity.json");
            return mapper.readValue(content, PokemonGoApiModel.RarityList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
