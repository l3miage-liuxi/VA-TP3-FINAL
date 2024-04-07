package fr.uga.l3miage.spring.tp3.exo2.Controller;

import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private EcosSessionRepository ecosSessionRepository;

    @AfterEach
    public void clear() {
        ecosSessionRepository.deleteAll();
    }

    @Test
    void canCreateSession(){
        final HttpHeaders headers = new HttpHeaders();
        Set<SessionProgrammationStepCreationRequest> steps=new HashSet<>();
        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest=SessionProgrammationStepCreationRequest
                .builder()
                .code("test")
                .description("description test")
                .build();

        steps.add(sessionProgrammationStepCreationRequest);

        SessionProgrammationCreationRequest sessionProgrammationCreationRequest=SessionProgrammationCreationRequest
                .builder()
                .label("test")
                .steps(steps)
                .build();

      final  SessionCreationRequest sessionCreationRequest=SessionCreationRequest
                .builder()
                .name("test")
                .examsId(Set.of())
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();

        ResponseEntity<SessionResponse> response = testRestTemplate
                .exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessionCreationRequest, headers), SessionResponse.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);

    }

    @Test
    void cannotCreateSession(){
        final HttpHeaders headers = new HttpHeaders();
        final  SessionCreationRequest sessionCreationRequest=SessionCreationRequest
                .builder()
                .name("test")
                .examsId(Set.of(114514L))
                .build();
        ResponseEntity<String> response = testRestTemplate
                .exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessionCreationRequest, headers), String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }
}
