package ie.ait.tavares.pogo.model.dao;

import ie.ait.tavares.pogo.model.entity.PokemonEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonEntryDao extends JpaRepository<PokemonEntry, Integer> {
}
