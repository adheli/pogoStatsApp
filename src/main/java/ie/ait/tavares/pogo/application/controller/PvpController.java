package ie.ait.tavares.pogo.application.controller;

import ie.ait.tavares.pogo.application.League;
import ie.ait.tavares.pogo.application.dto.Ranking;
import ie.ait.tavares.pogo.application.dto.StatsDto;
import ie.ait.tavares.pogo.external.api.pogostats.PogoStatsCalculator;
import ie.ait.tavares.pogo.model.dao.PokemonRankingDao;
import ie.ait.tavares.pogo.model.dao.PokemonStatsDao;
import ie.ait.tavares.pogo.model.entity.PokemonRanking;
import ie.ait.tavares.pogo.model.entity.PokemonStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/pvp")
public class PvpController {

    private final PogoStatsCalculator calculator;
    private final PokemonStatsDao statsDao;
    private final PokemonRankingDao rankingDao;

    @Autowired
    public PvpController(PogoStatsCalculator calculator, PokemonStatsDao statsDao, PokemonRankingDao rankingDao) {
        this.calculator = calculator;
        this.statsDao = statsDao;
        this.rankingDao = rankingDao;
    }

    @GetMapping
    public String getRankingPage(Model model) {
        List<PokemonStats> pokemons = statsDao.findAll();
        model.addAttribute("pokemons", pokemons);
        model.addAttribute("statsDto", new StatsDto());
        model.addAttribute("result", false);
        return "ranking";
    }

    @GetMapping("/result")
    public String checkPokemonStatsRanking(Model model, @RequestParam String pokemon, @RequestParam String levelCap,
                                           @RequestParam Integer attackIv, @RequestParam Integer defenseIv,
                                           @RequestParam Integer hpIv) {
        log.info("Checking ivs");
        StatsDto statsDto = new StatsDto(pokemon, attackIv, defenseIv, hpIv);
        statsDto.setLevelCap(levelCap);
        List<PokemonStats> pokemons = statsDao.findAll();
        PokemonStats poke = statsDao.findByName(pokemon);
        log.info(poke.toString());

        List<PokemonRanking> greatLeagueRank = rankingDao.findPokemonRankingByLeagueAndSpeciesIdStartsWith(
                League.GREAT.name(), poke.getName().toLowerCase());

        List<PokemonRanking> ultraLeagueRank = rankingDao.findPokemonRankingByLeagueAndSpeciesIdStartsWith(
                League.ULTRA.name(), poke.getName().toLowerCase());

        List<PokemonRanking> masterLeagueRank = rankingDao.findPokemonRankingByLeagueAndSpeciesIdStartsWith(
                League.MASTER.name(), poke.getName().toLowerCase());

        Ranking ranking = calculator.checkPokemonStats(statsDto, poke);

        log.info(ranking.getEvaluatedGreat().toString());
        log.info(ranking.getEvaluatedUltra().toString());
        log.info(ranking.getEvaluatedMaster().toString());

        model.addAttribute("pokemons", pokemons);
        model.addAttribute("statsDto", statsDto);
        model.addAttribute("result", true);
        model.addAttribute("rankingGreat", ranking.getEvaluatedGreat().toString());
        model.addAttribute("rankingUltra", ranking.getEvaluatedUltra().toString());
        model.addAttribute("rankingMaster", ranking.getEvaluatedMaster().toString());
        model.addAttribute("perfectGreat", ranking.getPerfectGreat().toString());
        model.addAttribute("perfectUltra", ranking.getPerfectUltra().toString());
        model.addAttribute("perfectMaster", ranking.getPerfectMaster().toString());
        model.addAttribute("greatLeagueRank", greatLeagueRank);
        model.addAttribute("ultraLeagueRank", ultraLeagueRank);
        model.addAttribute("masterLeagueRank", masterLeagueRank);
        return "ranking";
    }
}
