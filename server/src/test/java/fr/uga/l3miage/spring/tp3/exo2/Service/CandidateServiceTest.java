package fr.uga.l3miage.spring.tp3.exo2.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.services.CandidateService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateServiceTest {
    @Autowired
    private CandidateService candidateService;
    @MockBean
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;
    @MockBean
    private CandidateComponent candidateComponent;

    @Test
    void getCandidateAverageTest() throws CandidateNotFoundException{
        ExamEntity examEntity1=ExamEntity
            .builder()
            .id(114514L)
            .name("test1")
            .weight(1)
            .build();

            ExamEntity examEntity2=ExamEntity
            .builder()
            .id(1919810L)
            .name("test2")
            .weight(1)
            .build();

            ExamEntity examEntity3=ExamEntity
            .builder()
            .id(24L)
            .name("test3")
            .weight(1)
            .build();

            CandidateEvaluationGridEntity candidateEvaluationGridEntity1= CandidateEvaluationGridEntity
                .builder()
                .examEntity(examEntity3)
                .grade(15)
                .build();

            CandidateEvaluationGridEntity candidateEvaluationGridEntity2= CandidateEvaluationGridEntity
                .builder()
                .examEntity(examEntity3)
                .grade(10)
                .build();

            CandidateEvaluationGridEntity candidateEvaluationGridEntity3= CandidateEvaluationGridEntity
                .builder()
                .examEntity(examEntity2)
                .grade(5)
                .build();


            CandidateEntity candidateEntity=CandidateEntity
                .builder()
                .id(114L)
                .firstname("tiansuo")
                .lastname("haoer")
                .birthDate(LocalDate.of(1999,6,8))
                .email("allanWang@gmail.com")
                .hasExtraTime(false)
                .candidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity1,candidateEvaluationGridEntity2,candidateEvaluationGridEntity3))
                .build();

                when(candidateComponent.getCandidatById(114L)).thenReturn((candidateEntity));
                assert(candidateService.getCandidateAverage(114L) == 10.00);
    }
    @Test
    void testgetCandidateAverageNotFound() throws CandidateNotFoundException {

        when(candidateComponent.getCandidatById(anyLong())).thenThrow(CandidateNotFoundException.class);
        assertThrows(CandidateNotFoundRestException.class, () -> candidateService.getCandidateAverage(1L));
    }
}
