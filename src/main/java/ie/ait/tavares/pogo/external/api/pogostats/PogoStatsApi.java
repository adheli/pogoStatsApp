package ie.ait.tavares.pogo.external.api.pogostats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.ait.tavares.pogo.external.util.HttpConnectionHelper;
import ie.ait.tavares.pogo.model.entity.PokemonStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class PogoStatsApi {

    private static final String POGO_STATS_URL = "https://pogostat.com/pokedex.js";

    private String get() {
        try {
            HttpConnectionHelper helper = new HttpConnectionHelper(POGO_STATS_URL);
            return helper.processHttpCall();
        } catch (IOException e) {
            log.error("Cannot process http call to url {}}, error: {}", POGO_STATS_URL, e.getMessage());
            return "";
        }
    }

    public List<PokemonStats> pokeStatsList() {
        String content = get();
        String jsonContent = content.split("pokedex = ")[1].split(";")[0];
        log.info("did we get json?");
        log.info(jsonContent);
        try {
            List<PogoStatsModel> stats = new ObjectMapper().readerForListOf(PogoStatsModel.class).readValue(jsonContent);
            List<PokemonStats> pkStats = new ArrayList<>();
            stats.forEach(rawStat -> {
                    PokemonStats pk = new PokemonStats();
                    pk.setDex(rawStat.getId());
                    pk.setName(rawStat.getName());
                    pk.setAttack(rawStat.getAt());
                    pk.setDefense(rawStat.getDf());
                    pk.setHealthPoints(rawStat.getSt());

                    pkStats.add(pk);
            });
            return pkStats;
        } catch (JsonProcessingException e) {
            log.error("Cannot process content to map from json to class, error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
