package ie.ait.tavares.pogo.model.dao;

import ie.ait.tavares.pogo.model.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonDao extends JpaRepository<Pokemon, Integer> {

    @Query("select p from Pokemon p where p.legendary = true")
    List<Pokemon> findLegendary();

    @Query("select p from Pokemon p where p.shinyReleased = true")
    List<Pokemon> findShiny();

    @Query("select p from Pokemon p where p.released = true")
    List<Pokemon> findReleased();
}
