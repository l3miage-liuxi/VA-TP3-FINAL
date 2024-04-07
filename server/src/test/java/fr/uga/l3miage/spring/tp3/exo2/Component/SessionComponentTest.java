package fr.uga.l3miage.spring.tp3.exo2.Component;

import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionComponentTest {
    @Autowired
    private SessionComponent sessionComponent;

    @MockBean
    private EcosSessionRepository ecosSessionRepository;

    @MockBean
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;

    @MockBean
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;

    @Test
    void createSessionTest() {
        // Construct step entities
        EcosSessionProgrammationStepEntity step1 = EcosSessionProgrammationStepEntity
                .builder()
                .dateTime(LocalDateTime.now())
                .code("STEP1")
                .description("Description 1")
                .build();

        EcosSessionProgrammationStepEntity step2 = EcosSessionProgrammationStepEntity
                .builder()
                .dateTime(LocalDateTime.now())
                .code("STEP2")
                .description("Description 2")
                .build();

        // Construct session programmation with steps
        EcosSessionProgrammationEntity sessionProgrammation = EcosSessionProgrammationEntity.builder()
                .label("Programmation 1")
                .ecosSessionProgrammationStepEntities(new HashSet<>(Arrays.asList(step1,step2)))
                .build();

        // Construct session entity with session programmation
        EcosSessionEntity sessionEntity = EcosSessionEntity.builder()
                .name("Session 1")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(2))
                .ecosSessionProgrammationEntity(sessionProgrammation)
                .build();

        // Mock repository behavior
        when(ecosSessionProgrammationStepRepository.saveAll(anyCollection())).thenReturn(Arrays.asList(step1,step2));
        when(ecosSessionProgrammationRepository.save(any())).thenReturn(sessionProgrammation);
        when(ecosSessionRepository.save(any())).thenReturn(sessionEntity);

        // Call the method under test
        EcosSessionEntity result = sessionComponent.createSession(sessionEntity);

        // Assertions
        assertNotNull(result);
        assertEquals("Session 1", result.getName());
        assertNotNull(result.getEcosSessionProgrammationEntity());
        assertEquals(2, result.getEcosSessionProgrammationEntity().getEcosSessionProgrammationStepEntities().size());
    }
}
