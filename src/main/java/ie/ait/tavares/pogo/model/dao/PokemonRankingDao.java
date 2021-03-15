package ie.ait.tavares.pogo.model.dao;

import ie.ait.tavares.pogo.model.entity.PokemonRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRankingDao extends JpaRepository<PokemonRanking, Long> {

    List<PokemonRanking> findPokemonRankingByLeagueAndSpeciesIdStartsWith(String league, String species);
}
