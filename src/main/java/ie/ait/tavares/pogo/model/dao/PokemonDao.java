package ie.ait.tavares.pogo.model.dao;

import ie.ait.tavares.pogo.model.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonDao extends JpaRepository<Pokemon, Integer> {
}
