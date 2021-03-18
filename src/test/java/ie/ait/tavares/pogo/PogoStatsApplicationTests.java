package ie.ait.tavares.pogo;

import ie.ait.tavares.pogo.application.LoadPvpStatsInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class PogoStatsApplicationTests {

    @Autowired
    LoadPvpStatsInfo mockLoadInfo;

    @Test
    void contextLoads() {
        assertNotNull(mockLoadInfo);
    }

    @Configuration
    public static class TestConfiguration {
        @Bean
        @Primary
        public LoadPvpStatsInfo configurationService() {
            return Mockito.mock(LoadPvpStatsInfo.class);
        }
    }
}
