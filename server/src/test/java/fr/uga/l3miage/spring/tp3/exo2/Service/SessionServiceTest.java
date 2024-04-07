package fr.uga.l3miage.spring.tp3.exo2.Service;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void createSession() throws ExamNotFoundException {
        SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder()
                .build();
        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .build();
        SessionProgrammationCreationRequest sessionProgrammationCreationRequest = SessionProgrammationCreationRequest
                .builder()
                .build();
        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest = SessionProgrammationStepCreationRequest
                .builder()
                .build();
        ExamEntity examEntity = ExamEntity
                .builder()
                .id(1L)
                .build();

        sessionProgrammationCreationRequest.setSteps(Set.of(sessionProgrammationStepCreationRequest));
        sessionCreationRequest.setExamsId(Set.of(examEntity.getId()));
        sessionCreationRequest.setEcosSessionProgrammation(sessionProgrammationCreationRequest);


        when(sessionComponent.createSession(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);
        when(examComponent.getAllById(Set.of(anyLong()))).thenReturn(Set.of(examEntity));

        SessionResponse sessionResponseExpected = sessionMapper.toResponse(ecosSessionEntity);
        SessionResponse sessionResponseActual = sessionService.createSession(sessionCreationRequest);

        assertThat(sessionResponseActual).isEqualTo(sessionResponseExpected);

        
    }

    @Test
    void createSessionNotFound() throws ExamNotFoundException {
        when(examComponent.getAllById(Set.of(anyLong()))).thenThrow(ExamNotFoundException.class);
        assertThrows(CreationSessionRestException.class, () -> sessionService.createSession(any(SessionCreationRequest.class)));
    }
}



