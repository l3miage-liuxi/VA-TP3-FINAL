package fr.uga.l3miage.spring.tp3.exo2.Controller;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.services.CandidateService;
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
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private CandidateRepository candidateRepository;

    @AfterEach
    public void clear(){
        candidateRepository.deleteAll();
    }

    @Test
    void canGetCandidateAverage(){
        final HttpHeaders headers = new HttpHeaders();
        final Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("candidateId", 1L);
        CandidateEntity candidateEntity=CandidateEntity
            .builder()
            .id(1L)
            .email("")
            .build();
        ExamEntity examEntity = ExamEntity
            .builder()
            .weight(1)
            .build();
        CandidateEvaluationGridEntity candidateEvaluationGridEntity = CandidateEvaluationGridEntity
            .builder()
            .grade(10)
            .build();

        candidateEvaluationGridEntity.setExamEntity(examEntity);
        candidateEntity.setCandidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity));
        candidateRepository.save(candidateEntity);
        ResponseEntity<Double> rep = testRestTemplate.exchange("/api/candidates/{candidateId}/average", HttpMethod.GET, new HttpEntity<>(null, headers), Double.class,urlParams);
        assertThat(rep.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void cannotGetCandidateAverage(){
        final HttpHeaders headers = new HttpHeaders();
        final HashMap<String, Object> urlParam = new HashMap<>();
        urlParam.put("candidateId", "Le candidat n'a pas été trouvé");
        ResponseEntity<ChangeSetPersister.NotFoundException> rep = testRestTemplate.exchange("/api/candidates/{candidateId}", HttpMethod.GET, new HttpEntity<>(null, headers), ChangeSetPersister.NotFoundException.class, urlParam);
        assertThat(rep.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
