package fr.uga.l3miage.spring.tp3.exo2.Service;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import fr.uga.l3miage.spring.tp3.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {
    @Autowired
    private SessionService sessionService;

    @MockBean
    private ExamComponent examComponent;

    @MockBean
    private SessionComponent sessionComponent;

    @SpyBean
    private SessionMapper sessionMapper;

    @Test
    void createSession(){
        EcosSessionProgrammationStepEntity step1 = EcosSessionProgrammationStepEntity.builder()
                .description("Step 1 Description")
                .code("STEP1")
                .dateTime(LocalDateTime.now().plusHours(1))
                .build();

        EcosSessionProgrammationStepEntity step2 = EcosSessionProgrammationStepEntity.builder()
                .description("Step 2 Description")
                .code("STEP2")
                .dateTime(LocalDateTime.now().plusHours(2))
                .build();

        Set<EcosSessionProgrammationStepEntity> steps = new HashSet<>();
        steps.add(step1);
        steps.add(step2);

        EcosSessionProgrammationEntity programmation = EcosSessionProgrammationEntity.builder()
                .label("Programmation Label")
                .ecosSessionProgrammationStepEntities(steps)
                .build();

        SessionCreationRequest request  = SessionCreationRequest
                .builder()
                .name("Test Session")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(2))
                .ecosSessionProgrammationEntity(programmation)
                .build();





    }


}
