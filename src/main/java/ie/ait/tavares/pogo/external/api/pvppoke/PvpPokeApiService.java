package ie.ait.tavares.pogo.external.api.pvppoke;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.ait.tavares.pogo.application.League;
import ie.ait.tavares.pogo.external.util.HttpConnectionHelper;
import ie.ait.tavares.pogo.model.entity.PokemonRanking;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Getter
public class PvpPokeApiService {

    private static final String MASTER_LEAGUE = "rankings-10000";
    private static final String ULTRA_LEAGUE = "rankings-2500";
    private static final String GREAT_LEAGUE = "rankings-1500";

    @Value("${pvppoke.version}")
    private String apiVersion;
    @Value("${pvppoke.ranking.url}")
    private String apiUrl;

    private final ObjectMapper mapper = new ObjectMapper();

    private String get(String jsonEndPoint) {
        String url = String.format(apiUrl, jsonEndPoint, apiVersion);
        HttpConnectionHelper helper = new HttpConnectionHelper(url);
        try {
            return helper.processHttpCall();
        } catch (IOException e) {
            log.error("Cannot process http call to url {}}, error: {}", url, e.getMessage());
            return "";
        }
    }

    private List<PvpPokeModel> listPokemons(String ranking) {
        List<PvpPokeModel> pvpList = new ArrayList<>();

        try {
            String content = get(ranking);
            pvpList = mapper.readerForListOf(PvpPokeModel.class).readValue(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pvpList;
    }

    public List<PokemonRanking> greatLeague() {
        List<PokemonRanking> list = new ArrayList<>();
        listPokemons(GREAT_LEAGUE).forEach(rawPk -> list.add(parse(rawPk, League.GREAT.name())));
        return list;
    }

    public List<PokemonRanking> ultraLeague() {
        List<PokemonRanking> list = new ArrayList<>();
        listPokemons(ULTRA_LEAGUE).forEach(rawPk -> list.add(parse(rawPk, League.ULTRA.name())));
        return list;
    }

    public List<PokemonRanking> masterLeague() {
        List<PokemonRanking> list = new ArrayList<>();
        listPokemons(MASTER_LEAGUE).forEach(rawPk -> list.add(parse(rawPk, League.MASTER.name())));
        return list;
    }

    private PokemonRanking parse(PvpPokeModel rawPoke, String league) {
        PokemonRanking poke = new PokemonRanking();

        poke.setSpeciesId(rawPoke.getSpeciesId().split("_")[0]);
        poke.setSpeciesName(rawPoke.getSpeciesName());
        poke.setScore(rawPoke.getScore());
        poke.setFastMove(rawPoke.getMoveset().get(0));
        poke.setChargedOne(rawPoke.getMoveset().get(1));
        poke.setChargedTwo(rawPoke.getMoveset().size() > 2 ? rawPoke.getMoveset().get(2) : "");
        poke.setConsistency(rawPoke.getScores().size() > 2 ? rawPoke.getScores().get(rawPoke.getScores().size() - 1) : 0.0);
        poke.setLeague(league);

        return poke;
    }
}
