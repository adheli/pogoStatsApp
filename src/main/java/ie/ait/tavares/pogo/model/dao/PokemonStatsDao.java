package ie.ait.tavares.pogo.model.dao;

import ie.ait.tavares.pogo.model.entity.PokemonStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonStatsDao extends JpaRepository<PokemonStats, Long> {

    PokemonStats findByDex(Integer dex);

    PokemonStats findByName(String name);
}
