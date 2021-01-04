package ie.ait.tavares.pogo.model.service;

import ie.ait.tavares.pogo.model.dao.PokemonDao;
import ie.ait.tavares.pogo.model.dao.PokemonEntryDao;
import ie.ait.tavares.pogo.model.entity.Pokemon;
import ie.ait.tavares.pogo.model.entity.PokemonEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonEntryService {

    private final PokemonEntryDao entryDao;

    @Autowired
    public PokemonEntryService(PokemonEntryDao entryDao) {
        this.entryDao = entryDao;
    }

    public PokemonEntry saveEntry(PokemonEntry entry) {
        return entryDao.saveAndFlush(entry);
    }

    public PokemonEntry getEntry(Integer id) {
        return entryDao.findById(id).orElse(null);
    }

    public List<PokemonEntry> getEntries() {
        return entryDao.findAll();
    }

    public List<PokemonEntry> getMyShinies() {
        return getEntries().stream().filter(PokemonEntry::isShiny).collect(Collectors.toList());
    }

    public List<PokemonEntry> getMyLegendaries() {
        return getEntries().stream().filter(entry -> entry.getPokemon().isLegendary()).collect(Collectors.toList());
    }

    public List<PokemonEntry> getGreatLeague() {
        return getEntries().stream().filter(entry -> entry.getCombatPower() <= 1500).collect(Collectors.toList());
    }

    public List<PokemonEntry> getUltraLeague() {
        return getEntries().stream().filter(entry -> entry.getCombatPower() > 1500 && entry.getCombatPower() <= 2500).collect(Collectors.toList());
    }

    public List<PokemonEntry> getMasterLeague() {
        return getEntries().stream().filter(entry -> entry.getCombatPower() > 2500).collect(Collectors.toList());
    }

    public void deleteEntry(Integer id) {
        entryDao.deleteById(id);
    }
}
