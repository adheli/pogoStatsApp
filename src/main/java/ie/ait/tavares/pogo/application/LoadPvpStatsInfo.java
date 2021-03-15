package ie.ait.tavares.pogo.application;

import ie.ait.tavares.pogo.external.api.pogostats.PogoStatsApi;
import ie.ait.tavares.pogo.external.api.pvppoke.PvpPokeApiService;
import ie.ait.tavares.pogo.model.dao.ApiInfoDao;
import ie.ait.tavares.pogo.model.dao.PokemonRankingDao;
import ie.ait.tavares.pogo.model.dao.PokemonStatsDao;
import ie.ait.tavares.pogo.model.entity.ApiInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class LoadPvpStatsInfo implements ApplicationListener<ContextRefreshedEvent> {

    private final ApiInfoDao apiInfoDao;
    private final PokemonRankingDao rankingDao;
    private final PokemonStatsDao statsDao;
    private final PvpPokeApiService pvpApiService;
    private final PogoStatsApi statsApiService;

    @Autowired
    public LoadPvpStatsInfo(ApiInfoDao apiInfoDao, PokemonRankingDao rankingDao, PokemonStatsDao statsDao,
                            PvpPokeApiService pvpApiService, PogoStatsApi statsApiService) {
        this.apiInfoDao = apiInfoDao;
        this.rankingDao = rankingDao;
        this.statsDao = statsDao;
        this.pvpApiService = pvpApiService;
        this.statsApiService = statsApiService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadData();
    }

    private void loadData() {
        log.debug("Loading stuff here");
        List<ApiInfo> info = apiInfoDao.findAll();
        boolean load = true;
        if (info.isEmpty()) {
            ApiInfo apiInfo = new ApiInfo();
            apiInfo.setPvpPokeApiVersion(pvpApiService.getApiVersion());
            apiInfoDao.saveAndFlush(apiInfo);
        } else {
            if (info.get(0).getPvpPokeApiVersion().equalsIgnoreCase(pvpApiService.getApiVersion())) {
                load = false;
            } else {
                ApiInfo apiInfo = info.get(0);
                apiInfo.setPvpPokeApiVersion(pvpApiService.getApiVersion());
                apiInfoDao.saveAndFlush(apiInfo);
            }

        }

        if (load) {
            log.debug("Loading all pvp ranking");
            rankingDao.saveAll(pvpApiService.greatLeague());
            rankingDao.saveAll(pvpApiService.ultraLeague());
            rankingDao.saveAll(pvpApiService.masterLeague());

            log.debug("Loading all poke stats");
            statsDao.saveAll(statsApiService.pokeStatsList());
        }
    }
}
