package ie.ait.tavares.pogo.application.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

@SpringBootTest
@ActiveProfiles("test")
public class PokemonControllerTest {

    @Autowired
    PokemonController controller;

    Model model = new ConcurrentModel();

    @Test
    void testLoadPokemonData() {
//        controller.createPokemonData(model);
    }
}
